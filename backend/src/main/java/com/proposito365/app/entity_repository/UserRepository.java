package com.proposito365.app.entity_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
}
