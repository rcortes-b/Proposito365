package com.proposito365.app.domain.users.repository;

import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.proposito365.app.domain.users.domain.User;

import jakarta.persistence.EntityManager;

public class UserRepositoryImpl implements UserRepositoryCustom {
	private final static Logger logger = Logger.getLogger(UserRepositoryImpl.class);
	private EntityManager entity;

	public UserRepositoryImpl(EntityManager entity) {
		this.entity = entity;
	}
	@Transactional
	public Optional<User> findByUsernameOrEmail(String login) {
		String qlString = "SELECT u FROM User u WHERE u.username=:login OR u.email=:login";
		logger.info("[VAMO A VER 1]");
		User user = entity.createQuery(qlString, User.class)
								.setParameter("login", login)
								.getResultStream()
								.findFirst()
								.orElse(null);
		logger.info("[VAMO A VER 2]");
		return Optional.ofNullable(user);
	}
}
