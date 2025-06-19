package com.movieflix.subscriptionservice.exception;

public class IllegalArgumentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalArgumentException() {
		super("Illegal argument  found");
	}
	
	public IllegalArgumentException(String message) {
		super(message);
	}
	
	

}
