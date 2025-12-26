package com.proposito365.app.domain.users.repository;

import java.util.Optional;

import com.proposito365.app.domain.users.domain.User;

public interface UserRepositoryCustom {
	public Optional<User> findByUsernameOrEmail(String login);
}
