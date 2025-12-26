package com.proposito365.app.infrastructure.verification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
	An exception is thrown if the request is invalid
	The error codes are in the README.md / ERROR_CODES.md
*/

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
