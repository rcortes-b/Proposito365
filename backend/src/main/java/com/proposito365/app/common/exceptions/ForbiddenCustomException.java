package com.proposito365.app.common.exceptions;

public class ForbiddenCustomException extends RuntimeException {
	final private String code;

	public ForbiddenCustomException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
