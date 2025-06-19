package com.movieflix.subscriptionservice.service;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.movieflix.subscriptionservice.config.RabbitMQConfig;
import com.movieflix.subscriptionservice.dto.UserSubscriptionDTO;
import com.movieflix.subscriptionservice.entity.Plan;
import com.movieflix.subscriptionservice.entity.PlanName;
import com.movieflix.subscriptionservice.entity.UserSubscription;
import com.movieflix.subscriptionservice.exception.ResourceNotFoundException;
import com.movieflix.subscriptionservice.repository.PlanRepository;
import com.movieflix.subscriptionservice.repository.UserSubscriptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "subscription:user:";

    @Override
    public UserSubscriptionDTO subscribeUser(String userId, PlanName planName) {
        log.info("Subscribing user {} to plan {}", userId, planName);

        Plan plan = planRepository.findByName(planName)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + planName));

        // Deactivate existing
        subscriptionRepository.findByUserIdAndActiveTrue(userId).ifPresent(existingSub -> {
            existingSub.setActive(false);
            subscriptionRepository.save(existingSub);
            log.info("Deactivated existing subscription for user {}", userId);
        });

        UserSubscription subscription = UserSubscription.builder()
                .userId(userId)
                .plan(plan)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(plan.getDurationInDays()))
                .active(true)
                .build();

        UserSubscription saved = subscriptionRepository.save(subscription);
        UserSubscriptionDTO dto = mapToDTO(saved);

        // Emit event to RabbitMQ
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.SUBSCRIPTION_EXCHANGE,
            RabbitMQConfig.SUBSCRIPTION_ROUTING_KEY,
            "User " + userId + " subscribed to " + planName
        );

        // Cache in Redis
        redisTemplate.opsForValue().set(CACHE_PREFIX + userId, dto, 10, TimeUnit.MINUTES);
        log.info("Cached subscription info for user {}", userId);

        return dto;
    }

    @Override
    public boolean isUserSubscribed(String userId) {
        String key = CACHE_PREFIX + userId;

        if (redisTemplate.hasKey(key)) {
            log.info("Subscription info for user {} found in Redis", userId);
            return true;
        }

        boolean subscribed = subscriptionRepository.findByUserIdAndActiveTrue(userId).isPresent();
        log.info("Subscription check from DB for user {}: {}", userId, subscribed);

        return subscribed;
    }

    @Override
    public void cancelSubscription(String userId) {
        log.info("Cancelling subscription for user {}", userId);

        subscriptionRepository.findByUserIdAndActiveTrue(userId).ifPresent(subscription -> {
            subscription.setActive(false);
            subscriptionRepository.save(subscription);

            rabbitTemplate.convertAndSend(
                RabbitMQConfig.SUBSCRIPTION_EXCHANGE,
                RabbitMQConfig.SUBSCRIPTION_ROUTING_KEY,
                "User " + userId + " cancelled subscription"
            );

            redisTemplate.delete(CACHE_PREFIX + userId);
            log.info("Deleted Redis cache for user {}", userId);
        });
    }

    @Override
    public PlanName getUserPlanName(String userId) {
        log.info("Fetching plan name for user {}", userId);

        return subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .map(sub -> sub.getPlan().getName())
                .orElseThrow(() -> new ResourceNotFoundException("No active subscription found for user: " + userId));
    }

    @Override
    public UserSubscriptionDTO getActiveSubscription(String userId) {
        String key = CACHE_PREFIX + userId;

        UserSubscriptionDTO cached = (UserSubscriptionDTO) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.info("Active subscription for user {} fetched from Redis", userId);
            return cached;
        }

        UserSubscription sub = subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No active subscription found for user: " + userId));

        UserSubscriptionDTO dto = mapToDTO(sub);
        redisTemplate.opsForValue().set(key, dto, 10, TimeUnit.MINUTES);
        return dto;
    }

    private UserSubscriptionDTO mapToDTO(UserSubscription sub) {
        return UserSubscriptionDTO.builder()
                .userId(sub.getUserId())
                .planName(sub.getPlan().getName())
                .startDate(sub.getStartDate())
                .endDate(sub.getEndDate())
                .active(sub.isActive())
                .maxDevices(sub.getPlan().getMaxDevices())
                .allowedDevices(sub.getPlan().getAllowedDeviceTypes())
                .build();
    }
}
