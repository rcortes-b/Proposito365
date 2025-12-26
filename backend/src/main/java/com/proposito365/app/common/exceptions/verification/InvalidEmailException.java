package com.proposito365.app.common.exceptions.verification;

public class InvalidEmailException extends VerificationException {
	public InvalidEmailException() {
		super("EMAIL_NOT_VALID", "The new email already exists");
	}
}
