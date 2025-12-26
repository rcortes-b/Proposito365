package com.proposito365.app.domain.resolutions.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResolutionPostDTO(@NotBlank String resolution, @NotNull String details) {}
