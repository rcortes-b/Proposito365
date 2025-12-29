package com.proposito365.app.infrastructure.verification.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.proposito365.app.common.exceptions.verification.InvalidEmailException;
import com.proposito365.app.common.exceptions.verification.InvalidTokenException;
import com.proposito365.app.common.exceptions.verification.TokenExpiredException;
import com.proposito365.app.domain.users.domain.User;
import com.proposito365.app.domain.users.service.UserService;
import com.proposito365.app.infrastructure.middleware.auth.AuthService;
import com.proposito365.app.infrastructure.verification.domain.EmailVerification;
import com.proposito365.app.infrastructure.verification.domain.EmailVerificationDTO;
import com.proposito365.app.infrastructure.verification.domain.VerificationCodeGenerator;
import com.proposito365.app.infrastructure.verification.repository.EmailVerificationRepository;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
	private final static Logger logger = Logger.getLogger(EmailVerificationServiceImpl.class);
	private AuthService authService;
	private UserService userService;
	private EmailVerificationRepository emailVerificationRepository;
	
	@Autowired
    private JavaMailSender mailSender;

	public EmailVerificationServiceImpl(EmailVerificationRepository emailVerificationRepository,
										UserService userService, JavaMailSender mailSender,
											AuthService authService) {
		this.emailVerificationRepository = emailVerificationRepository;
		this.userService = userService;
		this.mailSender = mailSender;
		this.authService = authService;
	}

	@Override
	public void validateEmail(String email) {
		Optional<User> user = userService.getUserByEmail(email);
		if (user.isEmpty() == false)
			throw new InvalidEmailException();
	}

	@Override
	public String generateToken(String email) {
		final String verificationCode = VerificationCodeGenerator.generateVerificationCode();
		final Optional<EmailVerification> emailVerification = emailVerificationRepository.findByEmail(email);
		EmailVerification newEmailVerification;
		if (emailVerification.isEmpty()) {
			newEmailVerification = new EmailVerification();
			newEmailVerification.setEmail(email);
		} else {
			newEmailVerification = emailVerification.get();
		}
		newEmailVerification.setToken(verificationCode);
		newEmailVerification.setCreatedAt(Instant.now());
		newEmailVerification.setExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
		emailVerificationRepository.save(newEmailVerification);
		return verificationCode;
	}

	@Override
	public void sendVerificationEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification Code");
        message.setText("Your verification code is: " + verificationCode);
        mailSender.send(message);
	}

	/*
		First i check if a token exists in the database, then if the token has expired or not
		Compare the tokens and if is register i update the user to verified and if is email-change
		i just get the current user and update its email
	*/

	@Override
	public void verificateEmail(EmailVerificationDTO emailVerificationDTO) {
		EmailVerification emailVerification = emailVerificationRepository
											  .findByEmail(emailVerificationDTO.email())
											  .orElseThrow(InvalidEmailException::new);

		Instant currTime = Instant.now();
		logger.info("[EMAIL VERIF]: " + emailVerification.getExpiresAt());
		if (currTime.isAfter(emailVerification.getExpiresAt())) {
			emailVerificationRepository.delete(emailVerification);
			throw new TokenExpiredException();
		}
	
		logger.info("[INFOOOO] " + emailVerificationDTO.token() + " " + emailVerification.getToken());
		if (!emailVerificationDTO.token().equals(emailVerification.getToken()))
			throw new InvalidTokenException();

		Optional<User> user = userService.getUserByEmail(emailVerificationDTO.email());
		if (user.isEmpty() == false) {
			user.get().setVerified(true);
			userService.saveUser(user.get());
		} else {
			User newUser = authService.getAuthenticatedUser();
			newUser.setEmail(emailVerificationDTO.email());
			userService.saveUser(newUser);
			authService.updateCurrentUser(newUser);
			authService.generateCookies(null);
		}
	}
}
