package com.proposito365.app.common.exceptions;

import java.time.Instant;

public class ApiError {
	final private String code;
	final private String message;
	final private Instant timestamp;

	public ApiError(String code, String message) {
		this.code = code;
		this.message = message;
		this.timestamp = Instant.now();
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Instant getTimestamp() {
		return timestamp;
	}
}
