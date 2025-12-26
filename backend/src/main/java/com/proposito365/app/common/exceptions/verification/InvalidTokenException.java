package com.proposito365.app.common.exceptions.verification;

public class InvalidTokenException extends VerificationException {
	public InvalidTokenException() {
		super("INVALID_TOKEN", "Verification codes don't match");
	}
}
