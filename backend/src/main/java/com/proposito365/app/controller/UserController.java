package com.proposito365.app.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.dto.UserDTO;
import com.proposito365.app.models.User;
import com.proposito365.app.models.UserGroup;
import com.proposito365.app.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public UserDTO getUserInfo(Principal user) {
		return userService.getUserInfo(user);
	}

	@PatchMapping("/change-username")
	public User updateUserInfo(@RequestParam String username, Principal user) {
		return userService.updateUsername(username, user);
	}

	@DeleteMapping
	public User deleteUser(Principal user) {
		return userService.deleteUser(user);
	}

	@PostMapping("/{groupId}/join")
	public UserGroup joinGroup(@PathVariable Long groupId, Principal user) {
		return userService.joinGroup(groupId, user);
	}

	@PostMapping("/{groupId}/leave")
	public UserGroup leaveGroup(@PathVariable Long groupId, Principal user) {
		return userService.leaveGroup(groupId, user);
	}
	
}
