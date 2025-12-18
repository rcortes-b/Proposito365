package com.proposito365.app.middleware.auth.jwt;

import org.springframework.security.core.Authentication;

public interface TokenService {
	String generateToken(Authentication authentication, boolean isRefresh);
    String getUserFromToken(String token);
    boolean validateToken(String token);
}
