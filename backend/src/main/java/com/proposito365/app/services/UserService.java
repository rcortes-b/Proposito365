package com.proposito365.app.services;

import java.security.Principal;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proposito365.app.dto.RolesEnum;
import com.proposito365.app.dto.UserDTO;
import com.proposito365.app.exception.GroupNotFoundException;
import com.proposito365.app.exception.InvalidUserException;
import com.proposito365.app.exception.UserGroupRelationException;
import com.proposito365.app.exception.UserNotFoundException;
import com.proposito365.app.middleware.auth.AuthService;
import com.proposito365.app.models.Group;
import com.proposito365.app.models.Roles;
import com.proposito365.app.models.User;
import com.proposito365.app.models.UserGroup;
import com.proposito365.app.models.UserGroupId;
import com.proposito365.app.repository.GroupRepository;
import com.proposito365.app.repository.RolesRepository;
import com.proposito365.app.repository.UserGroupRepository;
import com.proposito365.app.repository.UserRepository;

/*
	- /api/users GET -> Get the user profile
	- /api/users PATCH -> Update info of the user
	- /api/users DELETE -> Delete the user account, also delete the user_resolutions and user_groups relations (i think the ON CASCADE does this!!!!). Also delete the cookies so you automatically logout
	- /api/users/{groupId}/join POST -> Check if group is not full and add 1 to group use capacity and add a user_group relation as 'MEMBER'
	- /api/users/{groupId}/leave POST -> Delete 1 to user capacity --- check if u're the admin and if the capacity is 0 delete also the group --- If u're the admin, you must change the admin to other
*/

@Service
@Transactional
public class UserService {
	private final static Logger logger = Logger.getLogger(UserService.class);
	private UserRepository userRepository;
	private GroupRepository groupRepository;
	private RolesRepository rolesRepository;
	private UserGroupRepository userGroupRepository;
	private AuthService authService;

	public UserService(UserRepository userRepository, GroupRepository groupRepository, 
						RolesRepository rolesRepository, UserGroupRepository userGroupRepository,
							AuthService authService) {
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
		this.rolesRepository = rolesRepository;
		this.userGroupRepository = userGroupRepository;
		this.authService = authService;
	}
	
	public User getUserByUsername(String userName) {
		Optional<User> user =  userRepository.findByUsernameOrEmail(userName);
		if (user.isEmpty())
			throw new UserNotFoundException();
		return user.get();
	}

	public UserDTO getUserInfo(Principal user) {
		User userInfo = getUserByUsername(user.getName());
		return new UserDTO(userInfo.getEmail(), userInfo.getUsername());
	}

	public User updateUsername(String username, Principal user) {
		User userInfo = getUserByUsername(user.getName());
		Optional<User> newUser = userRepository.findByUsername(username);
		if (newUser.isEmpty()) {
			throw new InvalidUserException(username + " already exists");
		} else {
			userInfo.setUsername(username);
			userRepository.save(userInfo);
			authService.updateCurrentUser(userInfo);
			authService.generateCookies();
		}
		return userInfo;
	}

	public void deleteUser(Principal user) {
		User userInfo = getUserByUsername(user.getName());
		logger.info("[USER SERVICE] Did the CASCADE work for groups/resolutions");
		userRepository.delete(userInfo);
	}

	public Group joinGroup(Long groupId, Principal user) {
		User userInfo = getUserByUsername(user.getName());
		Optional<Group> group = groupRepository.findById(groupId);
		if (group.isEmpty())
			throw new GroupNotFoundException();
		UserGroup userGroup = new UserGroup();
		userGroup.setUserGroupId(new UserGroupId(userInfo.getId(), groupId));
		userGroup.setUser(userInfo);
		userGroup.setGroup(group.get());
		Optional<Roles> role = rolesRepository.findByRole(RolesEnum.MEMBER.getDbValue());
		if (role == null) {
			/* This is a checker to know if this well defined because it is not TESTED !!! */
			logger.error("[USER SERVICE] Role wrong definition --- PERSONAL CHECKER");
			throw new RuntimeException();
		}
		userGroup.setRole(role.get());
		return group.get();
	}

	public void leaveGroup(Long groupId, Principal user) {
		User userInfo = getUserByUsername(user.getName());
		Optional<Group> group = groupRepository.findById(groupId);
		if (group.isEmpty())
			throw new GroupNotFoundException();
		Optional<UserGroup> userGroup = userGroupRepository.findByUserAndGroup(userInfo, group.get());
		if (userGroup.isEmpty())
			throw new UserGroupRelationException();
		userGroupRepository.delete(userGroup.get());
	}
}
