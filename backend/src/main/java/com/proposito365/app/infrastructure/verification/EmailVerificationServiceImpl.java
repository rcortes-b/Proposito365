package com.proposito365.app.infrastructure.verification;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.proposito365.app.common.exceptions.InvalidEmailException;
import com.proposito365.app.domain.users.domain.User;
import com.proposito365.app.domain.users.repository.UserRepository;
import com.proposito365.app.infrastructure.middleware.auth.AuthService;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

	private EmailVerificationRepository emailVerificationRepository;
	private UserRepository userRepository;
	private AuthService authService;
	
	@Autowired
    private JavaMailSender mailSender;

	public EmailVerificationServiceImpl(EmailVerificationRepository emailVerificationRepository,
										UserRepository userRepository, JavaMailSender mailSender,
											AuthService authService) {
		this.emailVerificationRepository = emailVerificationRepository;
		this.userRepository = userRepository;
		this.mailSender = mailSender;
		this.authService = authService;
	}

	@Override
	public void validateEmail(String email) {
		final Optional<User> user = userRepository.findByEmail(email);
		if (!user.isEmpty())
			throw new InvalidEmailException("EMAIL_NOT_VALID", "Email is already in use");
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

	@Override
	public void verificateEmail(EmailVerificationDTO emailVerificationDTO, Principal login) {
		Optional<EmailVerification> emailVerification = emailVerificationRepository.findByEmail(emailVerificationDTO.email());
		if (emailVerification.isEmpty())
			throw new InvalidEmailException("EMAIL_NOT_VALID", "This email doesn't need to be verified");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (timestamp.after(emailVerification.get().getExpiresAt())) {
			emailVerificationRepository.delete(emailVerification.get());
			throw new InvalidEmailException("TOKEN_EXPIRED", "The verification code has expired");
		}
		if (emailVerificationDTO.token() != emailVerification.get().getToken())
			throw new InvalidEmailException("INVALID_TOKEN", "The verification doesn't match");
		Optional<User> user = userRepository.findByEmail(emailVerificationDTO.email());
		if (!user.isEmpty()) {
			user.get().setVerified(true);
			userRepository.save(user.get());
		} else {
			Optional<User> newEmailUser = userRepository.findByUsernameOrEmail(login.getName());
			userRepository.delete(newEmailUser.get());
			newEmailUser.get().setEmail(emailVerificationDTO.email());
			userRepository.save(newEmailUser.get());
			authService.updateCurrentUser(newEmailUser.get());
			authService.generateCookies();
		}
	}
}
