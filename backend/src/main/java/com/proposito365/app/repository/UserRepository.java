package com.proposito365.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
}

/*	UserRepositoryCustom -> New interface with custom methods 
	UserRepositoryImpl -> New class implementing the custom interface `UserRepositoryCustom` methods
*/