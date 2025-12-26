package com.proposito365.app.domain.groups.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GroupCreationDTO(@NotEmpty String name, @NotNull String description) {}
