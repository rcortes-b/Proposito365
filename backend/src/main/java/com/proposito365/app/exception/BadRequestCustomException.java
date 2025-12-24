package com.proposito365.app.exception;

public class BadRequestCustomException extends RuntimeException {
	final private String code;

	public BadRequestCustomException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
