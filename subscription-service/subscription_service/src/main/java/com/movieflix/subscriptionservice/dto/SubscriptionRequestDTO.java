package com.movieflix.subscriptionservice.dto;

import com.movieflix.subscriptionservice.entity.PlanName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequestDTO {
    
    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Plan name is required")
    private PlanName planName;
}
