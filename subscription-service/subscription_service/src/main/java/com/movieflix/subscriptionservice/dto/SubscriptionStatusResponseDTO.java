package com.movieflix.subscriptionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionStatusResponseDTO {

	private Long userId;
	
	private boolean subscribed;
}
