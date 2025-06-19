package com.movieflix.subscriptionservice.dto;

import java.util.Set;

import com.movieflix.subscriptionservice.entity.DeviceType;
import com.movieflix.subscriptionservice.entity.PlanName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDTO {
	private Long id;
	private PlanName name;
	private Double price;
	private Integer durationInDays;
	private Integer maxDevices;
	private Set<DeviceType> allowedDeviceTypes;
}
