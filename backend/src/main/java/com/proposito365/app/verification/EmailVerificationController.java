package com.proposito365.app.verification;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.dto.EmailVerificationDTO;

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
	public ResponseEntity<Void> validateEmail(@RequestBody EmailVerificationDTO emailVerificationDTO,
												Principal login) {
		emailVerificationService.verificateEmail(emailVerificationDTO, login);
		return ResponseEntity.ok().build();
	}
}
