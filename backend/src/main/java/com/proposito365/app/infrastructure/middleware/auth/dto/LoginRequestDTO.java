package com.proposito365.app.infrastructure.middleware.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String login, @NotBlank String password) {}
