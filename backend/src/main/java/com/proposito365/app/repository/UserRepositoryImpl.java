package com.proposito365.app.repository;

import java.util.Optional;

import com.proposito365.app.models.User;

import jakarta.persistence.EntityManager;

public class UserRepositoryImpl implements UserRepositoryCustom {
	private EntityManager entity;

	public UserRepositoryImpl(EntityManager entity) {
		this.entity = entity;
	}

	public Optional<User> findByUsernameOrEmail(String login) {
		String qlString = "SELECT u FROM User u WHERE u.username=:login OR email=:login";

		User user = entity.createQuery(qlString, User.class)
								.setParameter("login", login)
								.getResultStream()
								.findFirst()
								.orElse(null);
		return Optional.ofNullable(user);
	}
}
