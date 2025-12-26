package com.proposito365.app.common.exceptions.verification;

public class VerificationException extends RuntimeException {
	 final String code;

	 public VerificationException(String code, String message) {
		 super(message);
		 this.code = code;
	 }

	 public String getCode() {
		return code;
	 }
}
