package com.proposito365.app.verification;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.dto.EmailVerificationDTO;

/*
	Register Steps
		- Frontend register form and request /auth/register
		- /auth/register -> Backend saves the user in the db with pending verification \
							and generates token (does a token exists for this user? yes -> override it, no->generate)
		- /verify-email -> Frontend sends the email + token in body -> Backend gets the user by email \
							checks if a token for the given user is defined and if is valid change the boolean exists of db
		**If user exists but is not verified -> back to /verify-email and option to regenerate a code
	Change email Steps
		- frontend asks for new email and request GET /email-validation
		- backend checks if the email exists -> if not exists -> generateToken return 200(does a token exists for this user? yes -> override it, no->generate)
		- frontend /verify-email -> Sends the newMail and the token
		- backend get the user by oldMail using Principal and check if token is valid if is valid 200 and /PATCH can be requestes
	
	Endpoints:
				- /email-verification
				- /change-email
*/

@RestController
@RequestMapping("/api")
public class EmailVerificationController {
	private EmailVerificationService emailVerificationService;

	public EmailVerificationController(EmailVerificationService emailVerificationService) {
		this.emailVerificationService = emailVerificationService;
	}

	@GetMapping("/email-validation")
	public ResponseEntity<EmailVerification> handleEmailValidation(@RequestParam String email) {
		final boolean isValid = emailVerificationService.isEmailValid(email);
		if (!isValid)
			return null;
		final String code =  emailVerificationService.generateToken(email);
		emailVerificationService.sendVerificationEmail(email, code);
		return ResponseEntity.ok(null);
	}

	@PostMapping("/email-verification")
	public EmailVerification validateEmail(@RequestBody EmailVerificationDTO emailVerificationDTO,
															Principal login) {
		final EmailVerification emailVerification = emailVerificationService.validateEmail(emailVerificationDTO, login);
		// In case of email-change i've to update the cookies so this architecture is maybe not the best fit :(
		return emailVerification;
	}
}
