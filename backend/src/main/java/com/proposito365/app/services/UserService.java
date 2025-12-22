package com.proposito365.app.services;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proposito365.app.domain.RolesEnum;
import com.proposito365.app.domain.UserDTO;
import com.proposito365.app.models.Group;
import com.proposito365.app.models.Roles;
import com.proposito365.app.models.User;
import com.proposito365.app.models.UserGroup;
import com.proposito365.app.models.UserGroupId;
import com.proposito365.app.repository.GroupRepository;
import com.proposito365.app.repository.RolesRepository;
import com.proposito365.app.repository.UserGroupRepository;
import com.proposito365.app.repository.UserRepository;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

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
	private JsonMapper jsonMapper;

	public UserService(UserRepository userRepository, GroupRepository groupRepository, 
						RolesRepository rolesRepository, UserGroupRepository userGroupRepository,
								JsonMapper jsonMapper) {
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
		this.rolesRepository = rolesRepository;
		this.userGroupRepository = userGroupRepository;
		this.jsonMapper = jsonMapper;
	}
	
	public User getUserByUsername(String userName) {
		Optional<User> user =  userRepository.findByUsernameOrEmail(userName);
		if (user.isEmpty())
			return null;
		return user.get();
	}

	public UserDTO getUserInfo(Principal user) {
		User userInfo = getUserByUsername(user.getName());
		if (userInfo == null)
			return null;

		return new UserDTO(userInfo.getEmail(), userInfo.getUsername());
	}

	public User updateUserInfo(Map<String, Object> patchPayload, Principal user) {
		User userInfo = getUserByUsername(user.getName());
		if (userInfo == null)
			return null;
		if (patchPayload.containsKey("id"))
			return null;
		if (patchPayload.containsKey("email"))
			logger.info("[USER SERVICE] Validate email!!!");

		User newUser = applyPatch(patchPayload, userInfo);
		logger.info("[USER SERVICE] If the username is modified, cookies must be updated!");
		userRepository.save(newUser);
		return newUser;
	}

	public User deleteUser(Principal user) {
		User userInfo = getUserByUsername(user.getName());
		if (userInfo == null)
			return null;
		logger.info("[USER SERVICE] Did the CASCADE work for groups/resolutions");
		userRepository.delete(userInfo);
		logger.info("[USER SERVICE] Necesito hacer logout automatico? Creo que de eso se encarga el front redirigiendolo!");
		return userInfo;
	}

	public UserGroup joinGroup(Long groupId, Principal user) {
		User userInfo = getUserByUsername(user.getName());
		Optional<Group> group = groupRepository.findById(groupId);
		if (userInfo == null || group.isEmpty())
			return null;

		UserGroup userGroup = new UserGroup();
		userGroup.setUserGroupId(new UserGroupId(userInfo.getId(), groupId));
		userGroup.setUser(userInfo);
		userGroup.setGroup(group.get());
		Optional<Roles> role = rolesRepository.findByRole(RolesEnum.MEMBER.getDbValue());
		if (role == null) {
			logger.error("[USER SERVICE] Role wrong definition");
			return null;
		}
		userGroup.setRole(role.get());
		return userGroup;
	}

	public UserGroup leaveGroup(Long groupId, Principal user) {
		User userInfo = getUserByUsername(user.getName());
		Optional<Group> group = groupRepository.findById(groupId);
		if (userInfo == null || group.isEmpty())
			return null;
		Optional<UserGroup> userGroup = userGroupRepository.findByUserAndGroup(userInfo, group.get());
		if (userGroup.isEmpty()) {
			logger.error("[USER SERVICE] User-Group relation does not exist");
			return null;
		}
		userGroupRepository.delete(userGroup.get());
		return userGroup.get();
	}

	private User applyPatch(Map<String, Object> patchPayload, User user) {
		ObjectNode objNode = jsonMapper.convertValue(user, ObjectNode.class);
		ObjectNode patchNode = jsonMapper.convertValue(patchPayload, ObjectNode.class);
		
		objNode.setAll(patchNode);
		return jsonMapper.convertValue(objNode, User.class);
	}

}
