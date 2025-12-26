package com.proposito365.app.common.exceptions.verification;

public class TokenExpiredException extends VerificationException {
	public TokenExpiredException() {
		super("TOKEN_EXPIRED", "Verification code has expired");
	}
}
