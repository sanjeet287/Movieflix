package com.movieflix.notificationservice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
    private String email;
    private String name;
    private String eventType; // like  "SIGNUP", "LOGIN", "UPDATE_PROFILE","DELETE"
}
