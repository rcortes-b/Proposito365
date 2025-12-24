package com.proposito365.app.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.dto.UserDTO;
import com.proposito365.app.models.Group;
import com.proposito365.app.models.User;
import com.proposito365.app.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<UserDTO> getUserInfo(Principal user) {
		UserDTO response = userService.getUserInfo(user);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/change-username")
	public ResponseEntity<User> updateUserInfo(@RequestParam String username, Principal user) {
		User response = userService.updateUsername(username, user);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteUser(Principal user, Authentication authentication) {
		userService.deleteUser(user);
		/* Delete cookies!!!!!!!!!! */
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{groupId}/join")
	public ResponseEntity<Group> joinGroup(@PathVariable Long groupId, Principal user) {
		Group group = userService.joinGroup(groupId, user);
		return ResponseEntity.ok(group);
	}

	@PostMapping("/{groupId}/leave")
	public ResponseEntity<Void> leaveGroup(@PathVariable Long groupId, Principal user) {
		userService.leaveGroup(groupId, user);
		return ResponseEntity.ok().build();
	}
	
}
