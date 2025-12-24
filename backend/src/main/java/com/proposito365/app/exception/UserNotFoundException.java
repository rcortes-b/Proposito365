package com.proposito365.app.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
        super(message);
    }
}
