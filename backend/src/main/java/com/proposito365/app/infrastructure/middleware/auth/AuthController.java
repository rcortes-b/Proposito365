package com.proposito365.app.infrastructure.middleware.auth;

import org.jboss.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.infrastructure.middleware.auth.dto.LoginRequestDTO;
import com.proposito365.app.infrastructure.middleware.auth.dto.RegisterRequestDTO;
import com.proposito365.app.infrastructure.verification.service.EmailVerificationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/*
	HttpServletRespones is used to communicate with the browser
*/


@RestController
public class AuthController {
	private static final Logger logger = Logger.getLogger(AuthController.class);
	private AuthService authService;
	private EmailVerificationService emailVerificationService;

	public AuthController(AuthService authService, EmailVerificationService emailVerificationService) {
		this.authService = authService;
		this.emailVerificationService = emailVerificationService;
	}
	
	@PostMapping("/auth/register")
	public void createUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO, HttpServletResponse response) {
		authService.createUser(registerRequestDTO);
		final String email = registerRequestDTO.email();
		final String code =  emailVerificationService.generateToken(email);
		emailVerificationService.sendVerificationEmail(email, code);
	}

	@PostMapping("/auth/login")
	public void login(@RequestBody @Valid LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
		authService.login(loginRequestDTO);
		authService.generateCookies();
	}

	/* Creates a new cookie with the same name and sets it to empty and expired
	   Then sends the cookie to the browser using HttpServletResponse
	*/
	@PostMapping("/auth/logout")
	public void logout(HttpServletResponse response) {
		final Cookie cookie = authService.createAuthCookie("", false);
		final Cookie cookieRefresh = authService.createAuthCookie("", true);
		cookie.setMaxAge(0);
		cookieRefresh.setMaxAge(0);
		response.addCookie(cookie);
		response.addCookie(cookieRefresh);
	}

	@PostMapping("/refresh")
	public void refresh(HttpServletResponse response, Authentication authentication) {

		UserSecurity userSecurity = (UserSecurity)authentication.getPrincipal();
		logger.info("[REFRESH]" + userSecurity.getUsername());
		final String accessToken = authService.generateToken(authentication, false);
		Cookie cookie = authService.createAuthCookie(accessToken, false);
		response.addCookie(cookie);
		/*
			General idea: authService validate, the authService inspect the 
			cookies looking for the refreshName, if not exists throws 401
			If exists -> get the jwt, decode it and based in the ID get the username
			then generateToken based in the username.
		*/
	}
}
