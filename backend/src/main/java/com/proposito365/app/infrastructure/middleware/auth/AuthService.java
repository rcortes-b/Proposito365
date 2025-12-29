package com.proposito365.app.infrastructure.middleware.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.proposito365.app.domain.users.domain.User;
import com.proposito365.app.infrastructure.middleware.auth.dto.LoginRequestDTO;
import com.proposito365.app.infrastructure.middleware.auth.dto.RegisterRequestDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    Authentication login(final LoginRequestDTO loginRequestDTO);
    boolean validateToken(final String token);
    String getUserFromToken(final String token);
	User getAuthenticatedUser();
    void createUser(final RegisterRequestDTO createUserDto);
    User getUser(Long id);
	String generateToken(Authentication authentication, boolean isRefresh);
	void validateRefreshCookie(HttpServletRequest request, HttpServletResponse response);
	UserDetails loadUserById(Long id);
	void generateCookies(Authentication authentication);
	void updateCurrentUser(User updatedUser);
	Cookie createAuthCookie(String token, boolean isRefresh);
	String getEncodedPassword(String password);
	void deleteCookies();
}
