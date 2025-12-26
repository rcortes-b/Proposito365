package com.proposito365.app.infrastructure.verification.service;

import com.proposito365.app.infrastructure.verification.domain.EmailVerificationDTO;

public interface EmailVerificationService {
	void validateEmail(String email);
	String generateToken(String email);
	void sendVerificationEmail(String toEmail, String code);
	void verificateEmail(EmailVerificationDTO emailVerificationDTO);
}
