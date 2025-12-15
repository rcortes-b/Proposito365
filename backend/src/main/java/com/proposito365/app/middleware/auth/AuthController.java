package com.proposito365.app.middleware.auth;

import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.middleware.auth.dto.LoginRequestDTO;
import com.proposito365.app.middleware.auth.dto.RegisterRequestDTO;
import com.proposito365.app.middleware.auth.utils.CookieProperties;

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
	private CookieProperties cookieProperties;

	public AuthController(AuthService authService, CookieProperties cookieProperties) {
		this.authService = authService;
		this.cookieProperties = cookieProperties;
	}
	
	@PostMapping("/auth/register")
	public void createUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO, HttpServletResponse response) {
		authService.createUser(registerRequestDTO);
	}

	@PostMapping("/auth/login")
	public void login(@RequestBody @Valid LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
		final String token = authService.login(loginRequestDTO);
		Cookie cookie = createAuthCookie(token);
		response.addCookie(cookie);
	}

	/* Creates a new cookie with the same name and sets it to empty and expired
	   Then sends the cookie to the browser using HttpServletResponse
	*/
	@PostMapping("/auth/logout")
	public void logout(HttpServletResponse response) {
		final Cookie cookie = new Cookie("auth-token", "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	@PostMapping("/refresh")
	public void refresh(HttpServletResponse response) {
		// Validate refresh token
			// Create New Cookie
				//response.addCookie()
	}
	
	private Cookie createAuthCookie(String token) {
        final String SAME_SITE_KEY = "SameSite";
        final Cookie cookie = new Cookie(cookieProperties.getName(), token);
        cookie.setHttpOnly(cookieProperties.isHttpOnly());
        cookie.setSecure(cookieProperties.isSecure());
		logger.info("[CONTROLLER INFO] Set max age is " + (int)cookieProperties.getMaxAge().getSeconds());
        cookie.setMaxAge((int)cookieProperties.getMaxAge().getSeconds());
        cookie.setAttribute(SAME_SITE_KEY, cookieProperties.getSameSite());
        return cookie;
    }
}
