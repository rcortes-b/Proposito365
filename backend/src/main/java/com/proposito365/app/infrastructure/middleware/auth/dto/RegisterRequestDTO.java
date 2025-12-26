package com.proposito365.app.infrastructure.middleware.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDTO(@NotBlank @Email String email,
									@NotBlank String username,
									@NotBlank 
									@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")
									String password)
{}
