package com.proposito365.app.common.exceptions;

public class InvalidEmailException extends RuntimeException {
	private final String code;
	public InvalidEmailException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
