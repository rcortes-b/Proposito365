package com.proposito365.app.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proposito365.app.models.User;
import com.proposito365.app.repository.UserRepository;
import com.proposito365.app.repository.UserRepositoryImpl;

@Service
@Transactional
public class UserService {
	private UserRepository userRepository;
	private UserRepositoryImpl userRepositoryImpl;

	public UserService(UserRepository userRepository, UserRepositoryImpl userRepositoryImpl) {
		this.userRepository = userRepository;
		this.userRepositoryImpl = userRepositoryImpl;
	}
	
	public User getUserByUsername(String userName) {
		Optional<User> user =  userRepositoryImpl.findByUsernameOrEmail(userName);
		if (user.isEmpty())
			return null;
		return user.get();
	}


}
