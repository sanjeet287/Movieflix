package com.movieflix.subscriptionservice.dto;

import com.movieflix.subscriptionservice.entity.DeviceType;
import com.movieflix.subscriptionservice.entity.PlanName;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSubscriptionDTO {

	private String userId;

	private PlanName planName;

	private LocalDate startDate;

	private LocalDate endDate;

	private boolean active;
	
	private Integer maxDevices;
	
	private Set<DeviceType> allowedDevices;

}
