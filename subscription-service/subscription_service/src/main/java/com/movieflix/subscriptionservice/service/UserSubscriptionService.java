package com.movieflix.subscriptionservice.service;

import com.movieflix.subscriptionservice.dto.UserSubscriptionDTO;
import com.movieflix.subscriptionservice.entity.PlanName;

public interface UserSubscriptionService {

    UserSubscriptionDTO subscribeUser(String userId, PlanName planName);

    boolean isUserSubscribed(String userId);

    void cancelSubscription(String userId);

    PlanName getUserPlanName(String userId);

    UserSubscriptionDTO getActiveSubscription(String userId);
}
