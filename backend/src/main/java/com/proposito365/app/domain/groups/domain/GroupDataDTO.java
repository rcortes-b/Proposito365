package com.proposito365.app.domain.groups.domain;

import java.util.List;

public record GroupDataDTO(String name, String description, List<String> usernames) {}
