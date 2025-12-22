package com.proposito365.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {}

/*	UserRepositoryCustom -> New interface with custom methods 
	UserRepositoryImpl -> New class implementing the custom interface `UserRepositoryCustom` methods
*/