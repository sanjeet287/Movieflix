package com.movieflix.notificationservice.entity;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class Notification {

	
	private String id;
    private String type;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;
}
