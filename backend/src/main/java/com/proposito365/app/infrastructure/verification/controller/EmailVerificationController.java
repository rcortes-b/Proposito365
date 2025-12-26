package com.proposito365.app.infrastructure.verification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.infrastructure.verification.domain.EmailVerificationDTO;
import com.proposito365.app.infrastructure.verification.service.EmailVerificationService;

@RestController
@RequestMapping("/api")
public class EmailVerificationController {
	private EmailVerificationService emailVerificationService;

	public EmailVerificationController(EmailVerificationService emailVerificationService) {
		this.emailVerificationService = emailVerificationService;
	}

	@PostMapping("/email-validation")
	public ResponseEntity<Void> handleEmailValidation(@RequestParam String email) {
		emailVerificationService.validateEmail(email);
		final String code =  emailVerificationService.generateToken(email);
		emailVerificationService.sendVerificationEmail(email, code);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/email-verification")
	public ResponseEntity<Void> validateEmail(@RequestBody EmailVerificationDTO emailVerificationDTO) {
		emailVerificationService.verificateEmail(emailVerificationDTO);
		return ResponseEntity.ok().build();
	}
}
