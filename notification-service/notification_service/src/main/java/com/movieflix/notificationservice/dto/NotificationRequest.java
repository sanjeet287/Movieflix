package com.movieflix.notificationservice.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
	
	private String toEmail;
    private String subject;
    private String templateName; // e.g. "emailTemplate"
    private Map<String, Object> variables; // e.g. username, actionUrl, message

}

