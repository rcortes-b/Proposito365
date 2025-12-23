package com.proposito365.app.verification;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.proposito365.app.dto.EmailVerificationDTO;
import com.proposito365.app.models.User;
import com.proposito365.app.repository.UserRepository;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

	private EmailVerificationRepository emailVerificationRepository;
	private UserRepository userRepository;
	
	@Autowired
    private JavaMailSender mailSender;

	public EmailVerificationServiceImpl(EmailVerificationRepository emailVerificationRepository,
										UserRepository userRepository, JavaMailSender mailSender) {
		this.emailVerificationRepository = emailVerificationRepository;
		this.userRepository = userRepository;
		this.mailSender = mailSender;
	}

	@Override
	public boolean isEmailValid(String email) {
		final Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty())
			return true;
		return false;
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
	public EmailVerification validateEmail(EmailVerificationDTO emailVerificationDTO, Principal login) {
		Optional<EmailVerification> emailVerification = emailVerificationRepository.findByEmail(emailVerificationDTO.email());
		if (emailVerification.isEmpty())
			return null;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (timestamp.after(emailVerification.get().getExpiresAt()))
			return null;
		if (emailVerificationDTO.token() != emailVerification.get().getToken())
			return null;
		//register -> exists but not logged in   -- set as verified
		//change-email -> doesnt exists but you're loggedin -- update the email but delete the old emailVerif entity
		Optional<User> user = userRepository.findByEmail(emailVerificationDTO.email());
		if (!user.isEmpty()) {
			user.get().setVerified(true);
			userRepository.save(user.get());
		} else {
			Optional<User> newEmailUser = userRepository.findByUsernameOrEmail(login.getName());
			userRepository.delete(newEmailUser.get());
			newEmailUser.get().setEmail(emailVerificationDTO.email());
			userRepository.save(newEmailUser.get());
		}
		return emailVerification.get();
	}

	@Override
	public void deleteEmailVerification(Long id) {
	
	}

	
}
