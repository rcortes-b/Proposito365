package com.proposito365.app.infrastructure.middleware.auth;

import java.nio.file.ProviderNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.proposito365.app.common.exceptions.UserNotFoundException;
import com.proposito365.app.domain.users.domain.User;
import com.proposito365.app.domain.users.repository.UserRepository;
import com.proposito365.app.infrastructure.middleware.auth.dto.LoginRequestDTO;
import com.proposito365.app.infrastructure.middleware.auth.dto.RegisterRequestDTO;
import com.proposito365.app.infrastructure.middleware.auth.jwt.TokenService;
import com.proposito365.app.infrastructure.middleware.auth.utils.CookieProperties;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
	private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);
	
	private UserRepository userRepository;
	private TokenService tokenService;
	private PasswordEncoder passwordEncoder;
	private CookieProperties cookieProperties;
	private AuthenticationConfiguration authenticationConfiguration;

	public AuthServiceImpl(UserRepository userRepository, TokenService tokenService,
							PasswordEncoder passwordEncoder, CookieProperties cookieProperties,
								AuthenticationConfiguration authenticationConfiguration) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
		this.passwordEncoder = passwordEncoder;
		this.cookieProperties = cookieProperties;
		this.authenticationConfiguration = authenticationConfiguration;
	} 

	@Override
	public void createUser(final RegisterRequestDTO createUserDto) {
		final User newUser = AuthMapper.fromDto(createUserDto);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setVerified(false);
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
		Optional<User> user = userRepository.findByUsernameOrEmail(login);

		if (user.isEmpty())
			logger.error("[USER] : User not found with login: " + login + " --- Remember add the Exception 2!!!");
		logger.info("[USER] : User successfully obtained with username " + user.get().getUsername());
		return new UserSecurity(user.get());
	}


	@Override
	public String generateToken(Authentication authentication, boolean isRefresh) {
		return tokenService.generateToken(authentication, isRefresh);
	}

	@Override
	public void validateRefreshCookie(HttpServletRequest request, HttpServletResponse response) {
		final Cookie[] cookies = request.getCookies();
		
		if (cookies == null || cookies.length == 0) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new BadCredentialsException("Invalid refresh token");
		}
		CookieProperties cookieProperties = new CookieProperties();
		Optional<String> token = Arrays.stream(cookies)
            						   .filter(cookie -> cookie.getName().equals(cookieProperties.getNameRefresh()))
            						   .map(Cookie::getValue)
            						   .findFirst();
		if (token.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new BadCredentialsException("Invalid refresh token");
		}
	}

	@Override
	public void generateCookies() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = attrs.getResponse();

		final String accessToken = generateToken(authentication, false);
		final String refreshToken = generateToken(authentication, true);
		final Cookie cookie = createAuthCookie(accessToken, false);
		final Cookie cookieRefresh = createAuthCookie(refreshToken, true);
		response.addCookie(cookie);
		response.addCookie(cookieRefresh);
	}

	@Override
	public Cookie createAuthCookie(String token, boolean isRefresh) {
		logger.info("[COOKIE INFO]: " + cookieProperties.toString() + "      " + token);
        final String SAME_SITE_KEY = "SameSite";
		final String name = isRefresh == true ? cookieProperties.getNameRefresh() : cookieProperties.getName();
		final int max_age = (int)(isRefresh == true ? cookieProperties.getMaxAgeRefresh()
								: cookieProperties.getMaxAge()).getSeconds();
        final Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(cookieProperties.isHttpOnly());
        cookie.setSecure(cookieProperties.isSecure());
		cookie.setPath("/");
        cookie.setMaxAge(max_age);
        cookie.setAttribute(SAME_SITE_KEY, cookieProperties.getSameSite());
        return cookie;
    }

	@Override
	public void updateCurrentUser(User updatedUser) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Authentication newAuth = new UsernamePasswordAuthenticationToken(
				updatedUser, 
				auth.getCredentials(), 
				null
		);
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	public User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		User loggedUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		return loggedUser;
	}

	@Override
	public UserDetails loadUserById(Long id) {
		logger.info("VA LOKO  " + id);
		Optional<User> user = userRepository.findById(id);
		logger.info("VA LOKOK");
		if (user.isEmpty())
			logger.error("[USER] : User not found with id: " + id + " --- Remember add the Exception 3!!!");
		logger.info("[USER] : User successfully obtained with username " + user.get().getUsername());
		return new UserSecurity(user.get());
	}

	@Override
	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}
}
