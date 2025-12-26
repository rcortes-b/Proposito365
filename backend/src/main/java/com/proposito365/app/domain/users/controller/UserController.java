package com.proposito365.app.domain.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.domain.users.domain.PasswordDTO;
import com.proposito365.app.domain.users.domain.User;
import com.proposito365.app.domain.users.domain.UserDTO;
import com.proposito365.app.domain.users.service.UserService;
import com.proposito365.app.infrastructure.middleware.auth.AuthService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;
	private AuthService authService;

	public UserController(UserService userService, AuthService authService) {
		this.userService = userService;
		this.authService = authService;
	}

	@GetMapping
	public ResponseEntity<UserDTO> getUserInfo() {
		UserDTO response = userService.getUserInfo();
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/change-username")
	public ResponseEntity<User> updateUserInfo(@RequestParam String username) {
		User response = userService.updateUsername(username);
		authService.updateCurrentUser(response);
		authService.generateCookies();
		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteUser() {
		userService.deleteUser(authService.getAuthenticatedUser());
		authService.deleteCookies();
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/change-password")
	public ResponseEntity<Void> changePassword(@RequestBody PasswordDTO passwordDTO) {
		userService.changePassword(passwordDTO);
		return ResponseEntity.ok().build();
	}
}
