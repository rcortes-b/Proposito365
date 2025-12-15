package com.proposito365.app.repository;

import java.util.Optional;

import com.proposito365.app.models.User;

public interface UserRepositoryCustom {
	public Optional<User> findByUsernameOrEmail(String login);
}
