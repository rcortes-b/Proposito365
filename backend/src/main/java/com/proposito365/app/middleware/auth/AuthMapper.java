package com.proposito365.app.middleware.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.proposito365.app.middleware.auth.dto.LoginRequestDTO;
import com.proposito365.app.middleware.auth.dto.RegisterRequestDTO;
import com.proposito365.app.middleware.auth.dto.UserResponseDTO;
import com.proposito365.app.models.User;

public class AuthMapper {
	private AuthMapper() {}
	
	public static User fromDto(RegisterRequestDTO registerRequestDTO) {
		return new User(registerRequestDTO.email(), registerRequestDTO.username(), registerRequestDTO.password());
	}

	public static Authentication fromDto(LoginRequestDTO loginRequestDTO) {
		return new UsernamePasswordAuthenticationToken(loginRequestDTO.login(), loginRequestDTO.password());
	}

	public static UserResponseDTO toDto(final User user) {
		return new UserResponseDTO(user.getId(), user.getEmail(), user.getUsername());
	}
}
