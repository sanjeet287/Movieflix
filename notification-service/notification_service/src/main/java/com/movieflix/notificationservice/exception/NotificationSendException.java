package com.movieflix.notificationservice.exception;

public class NotificationSendException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotificationSendException(String message, Throwable cause) {
        super(message, cause);
    }
}

