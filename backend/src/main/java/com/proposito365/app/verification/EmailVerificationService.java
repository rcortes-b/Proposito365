package com.proposito365.app.verification;

import java.security.Principal;

import com.proposito365.app.dto.EmailVerificationDTO;

public interface EmailVerificationService {
	void validateEmail(String email);
	String generateToken(String email);
	void sendVerificationEmail(String toEmail, String code);
	void verificateEmail(EmailVerificationDTO emailVerificationDTO, Principal login);
}
