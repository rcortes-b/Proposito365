package com.proposito365.app.domain.users.service;

import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proposito365.app.common.exceptions.InvalidPasswordException;
import com.proposito365.app.common.exceptions.InvalidUserException;
import com.proposito365.app.common.exceptions.UserNotFoundException;
import com.proposito365.app.domain.users.domain.PasswordDTO;
import com.proposito365.app.domain.users.domain.User;
import com.proposito365.app.domain.users.domain.UserDTO;
import com.proposito365.app.domain.users.repository.UserRepository;
import com.proposito365.app.infrastructure.middleware.auth.AuthService;

/*
	- /api/users GET -> Get the user profile
	- /api/users PATCH -> Update info of the user
	- /api/users DELETE -> Delete the user account, also delete the user_resolutions and user_groups relations (i think the ON CASCADE does this!!!!). Also delete the cookies so you automatically logout
*/

@Service
@Transactional
public class UserService {
	private static final Logger logger = Logger.getLogger(UserService.class);
	private AuthService authService;
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public UserService(AuthService authService, UserRepository userRepository,
						PasswordEncoder passwordEncoder) {
		this.authService = authService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User getUserByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		return user;
	}

	public UserDTO getUserInfo() {
		User user = authService.getAuthenticatedUser();
		return new UserDTO(user.getEmail(), user.getUsername());
	}

	public User updateUsername(String newUsername) {
		User user = authService.getAuthenticatedUser();
		Optional<User> newUser = userRepository.findByUsername(newUsername);
		if (!newUser.isEmpty()) {
			throw new InvalidUserException(newUsername + " already exists");
		} else {
			user.setUsername(newUsername);
			userRepository.save(user);
		}
		return user;
	}

	public void saveUser(User user) {
		if (user != null)
			userRepository.save(user);
	}

	public void deleteUser(User user) {
		if (user != null)
			userRepository.delete(user);
	}

	public void changePassword(PasswordDTO passwordDTO) {
		User user = authService.getAuthenticatedUser();
		logger.info("[USER SERVICE] Password: " + user.getPassword());
		if (!passwordEncoder.matches(passwordDTO.oldPassword(), user.getPassword()))
			throw new InvalidPasswordException("The password doesn't match");
		if (!passwordEncoder.matches(passwordDTO.newPassword(), user.getPassword()))
			throw new InvalidPasswordException("The new password cannot be the same than the old");
		user.setPassword(passwordEncoder.encode(passwordDTO.newPassword()));
		userRepository.save(user);
	}
}
