package com.proposito365.app.domain.users.domain;

import jakarta.validation.constraints.NotEmpty;

public record PasswordDTO(@NotEmpty String oldPassword, @NotEmpty String newPassword) {}
