package com.proposito365.app.domain.users.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordDTO(@NotBlank String oldPassword, @NotBlank 
														@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")
														String newPassword) {}
