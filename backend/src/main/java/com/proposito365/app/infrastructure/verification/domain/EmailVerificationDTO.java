package com.proposito365.app.infrastructure.verification.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerificationDTO(@NotBlank @Email String email, @NotBlank String token) {}
