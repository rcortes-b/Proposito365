package com.proposito365.app.middleware.auth;

import java.nio.file.ProviderNotFoundException;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proposito365.app.middleware.auth.dto.LoginRequestDTO;
import com.proposito365.app.middleware.auth.dto.RegisterRequestDTO;
import com.proposito365.app.middleware.auth.jwt.TokenService;
import com.proposito365.app.models.User;
import com.proposito365.app.repository.UserRepository;
import com.proposito365.app.repository.UserRepositoryImpl;


@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
	private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);
	
	private UserRepository userRepository;
	private UserRepositoryImpl userRepositoryImpl;
	private TokenService tokenService;
	private PasswordEncoder passwordEncoder;
	private AuthenticationConfiguration authenticationConfiguration;

	public AuthServiceImpl(UserRepository userRepository, UserRepositoryImpl userRepositoryImpl,
							TokenService tokenService, PasswordEncoder passwordEncoder,
								AuthenticationConfiguration authenticationConfiguration) {
		this.userRepository = userRepository;
		this.userRepositoryImpl = userRepositoryImpl;
		this.tokenService = tokenService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationConfiguration = authenticationConfiguration;
	} 

	@Override
	public void createUser(final RegisterRequestDTO createUserDto) {
		final User newUser = AuthMapper.fromDto(createUserDto);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		final User finalUser = userRepository.save(newUser);
		logger.info("[USER] : User successfully created with id " + finalUser.getId());
	}

	@Override
	public User getUser(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			logger.error("[USER] : User not found with id " + id + " --- Remember add the Exception 1!!!");
			return new User();
		}
		logger.info("[USER] : User successfully obtained with id " + user.get().getId());
		return user.get();
	}

	@Override
	public String getUserFromToken(final String token) {
		return tokenService.getUserFromToken(token);
	}

	@Override
	public Authentication login(final LoginRequestDTO loginRequestDTO) {
		try {
			final AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            final Authentication authRequest = AuthMapper.fromDto(loginRequestDTO);
            return  authenticationManager.authenticate(authRequest);
		} catch (Exception exception) {
			logger.error("[USER] : Error while trying to login", exception);
            throw new ProviderNotFoundException("Error while trying to login");
		}
	}

	@Override
	public boolean validateToken(final String token) {
		tokenService.validateToken(token);
		return true;
	}

	@Override
	public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
		Optional<User> user = userRepositoryImpl.findByUsernameOrEmail(login);
		if (user.isEmpty())
			logger.error("[USER] : User not found with login: " + login + " --- Remember add the Exception 2!!!");
		logger.info("[USER] : User successfully obtained with username " + user.get().getUsername());
		return new UserSecurity(user.get());
	}

	@Override
	public String generateToken(Authentication authentication, boolean isRefresh) {
		return tokenService.generateToken(authentication, isRefresh);
	}
	
}
