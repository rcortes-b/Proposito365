package com.proposito365.app.domain.groups.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroupCreationDTO(@NotBlank String name, @NotNull String description) {}
