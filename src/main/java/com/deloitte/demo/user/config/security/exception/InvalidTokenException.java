package com.deloitte.demo.user.config.security.exception;

public class InvalidTokenException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTokenException() {
	}

	public InvalidTokenException(String message) {
		
		super(message);
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return null;
	}
	
}