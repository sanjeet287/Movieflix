package com.movieflix.subscriptionservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieflix.subscriptionservice.entity.UserSubscription;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

	Optional<UserSubscription> findByUserIdAndActiveTrue(String userId);
}
