package com.proposito365.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proposito365.app.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
}
