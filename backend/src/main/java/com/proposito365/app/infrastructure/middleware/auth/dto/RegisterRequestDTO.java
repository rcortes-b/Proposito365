package com.proposito365.app.infrastructure.middleware.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(@NotBlank @Email String email,
									@NotBlank String username,
									@NotBlank String password)
{}
