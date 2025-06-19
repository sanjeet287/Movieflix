package com.movieflix.subscriptionservice.controller;

import com.movieflix.subscriptionservice.dto.SubscriptionRequestDTO;
import com.movieflix.subscriptionservice.dto.UserSubscriptionDTO;
import com.movieflix.subscriptionservice.entity.PlanName;
import com.movieflix.subscriptionservice.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class UserSubscriptionController {

    private final UserSubscriptionService subscriptionService;

    @PostMapping("/{userId}/subscribe")
    public ResponseEntity<UserSubscriptionDTO> subscribeUser(
    		@RequestBody SubscriptionRequestDTO request
    ) {
        log.info("Subscribing user {} to plan {}", request.getUserId(), request.getPlanName());
        UserSubscriptionDTO dto = subscriptionService.subscribeUser(request.getUserId(), request.getPlanName());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{userId}/active")
    public ResponseEntity<UserSubscriptionDTO> getActiveSubscription(@PathVariable String userId) {
        log.info("Getting active subscription for user {}", userId);
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(userId));
    }

    @GetMapping("/{userId}/is-subscribed")
    public ResponseEntity<Boolean> isUserSubscribed(@PathVariable String userId) {
        log.info("Checking if user {} is subscribed", userId);
        return ResponseEntity.ok(subscriptionService.isUserSubscribed(userId));
    }

    @GetMapping("/{userId}/plan")
    public ResponseEntity<PlanName> getUserPlan(@PathVariable String userId) {
        log.info("Fetching current plan name for user {}", userId);
        return ResponseEntity.ok(subscriptionService.getUserPlanName(userId));
    }

    @DeleteMapping("/{userId}/cancel")
    public ResponseEntity<Void> cancelSubscription(@PathVariable String userId) {
        log.info("Cancelling subscription for user {}", userId);
        subscriptionService.cancelSubscription(userId);
        return ResponseEntity.noContent().build();
    }
}
