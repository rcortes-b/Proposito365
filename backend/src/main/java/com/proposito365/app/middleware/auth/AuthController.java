package com.proposito365.app.middleware.auth;

import org.jboss.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.middleware.auth.dto.LoginRequestDTO;
import com.proposito365.app.middleware.auth.dto.RegisterRequestDTO;
import com.proposito365.app.middleware.auth.jwt.TokenService;
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
		Authentication authentication = authService.login(loginRequestDTO);
		final String accessToken = authService.generateToken(authentication, false);
		final String refreshToken = authService.generateToken(authentication, true);
		final Cookie cookie = createAuthCookie(accessToken, false);
		final Cookie cookieRefresh = createAuthCookie(refreshToken, true);
		response.addCookie(cookie);
		response.addCookie(cookieRefresh);
	}

	/* Creates a new cookie with the same name and sets it to empty and expired
	   Then sends the cookie to the browser using HttpServletResponse
	*/
	@PostMapping("/auth/logout")
	public void logout(HttpServletResponse response) {
		logger.info("LLEGO???");
		final Cookie cookie = createAuthCookie("", false);
		final Cookie cookieRefresh = createAuthCookie("", true);
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
		Cookie cookie = createAuthCookie(accessToken, false);
		response.addCookie(cookie);
		/*
			General idea: authService validate, the authService inspect the 
			cookies looking for the refreshName, if not exists throws 401
			If exists -> get the jwt, decode it and based in the ID get the username
			then generateToken based in the username.
		*/
	}
	
	private Cookie createAuthCookie(String token, boolean isRefresh) {
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
}
