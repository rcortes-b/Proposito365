package com.proposito365.app.middleware.auth;

import org.springframework.security.core.Authentication;

import com.proposito365.app.middleware.auth.dto.LoginRequestDTO;
import com.proposito365.app.middleware.auth.dto.RegisterRequestDTO;
import com.proposito365.app.models.User;

public interface AuthService {
    Authentication login(final LoginRequestDTO loginRequestDTO);
    boolean validateToken(final String token);
    String getUserFromToken(final String token);
    void createUser(final RegisterRequestDTO createUserDto);
    User getUser(Long id);
	String generateToken(Authentication authentication, boolean isRefresh);
}
