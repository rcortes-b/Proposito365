package com.proposito365.app.verification;

import java.security.Principal;

import com.proposito365.app.dto.EmailVerificationDTO;

public interface EmailVerificationService {
	boolean isEmailValid(String email);
	String generateToken(String email);
	void sendVerificationEmail(String toEmail, String code);
	EmailVerification validateEmail(EmailVerificationDTO emailVerificationDTO, Principal login);
	void deleteEmailVerification(Long id);
}
