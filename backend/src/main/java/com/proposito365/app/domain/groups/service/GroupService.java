package com.proposito365.app.domain.groups.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import com.proposito365.app.common.exceptions.BadRequestCustomException;
import com.proposito365.app.common.exceptions.GroupFullCapacityException;
import com.proposito365.app.common.exceptions.GroupNotFoundException;
import com.proposito365.app.common.exceptions.InvalidGroupAdminException;
import com.proposito365.app.domain.groups.domain.Group;
import com.proposito365.app.domain.groups.domain.GroupCreationDTO;
import com.proposito365.app.domain.groups.domain.GroupDataDTO;
import com.proposito365.app.domain.groups.repository.GroupRepository;
import com.proposito365.app.domain.resolutions.domain.ResolutionGetDTO;
import com.proposito365.app.domain.resolutions.service.ResolutionService;
import com.proposito365.app.domain.roles.Roles;
import com.proposito365.app.domain.roles.RolesEnum;
import com.proposito365.app.domain.roles.RolesRepository;
import com.proposito365.app.domain.user_group.UserGroup;
import com.proposito365.app.domain.user_group.UserGroupService;
import com.proposito365.app.domain.users.domain.User;
import com.proposito365.app.domain.users.service.UserService;
import com.proposito365.app.infrastructure.middleware.auth.AuthService;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

@Service
public class GroupService {
	public final static Logger logger = Logger.getLogger(GroupService.class);
	private AuthService authService;
	private UserService userService;
	private UserGroupService userGroupService;
	private ResolutionService resolutionService;
	private GroupRepository groupRepository;
	private RolesRepository rolesRepository;
	private JsonMapper jsonMapper;

	public GroupService(AuthService authService, UserService userService, UserGroupService userGroupService,
						ResolutionService resolutionService, GroupRepository groupRepository,
							RolesRepository rolesRepository, JsonMapper jsonMapper) {
		this.authService = authService;
		this.userService = userService;
		this.userGroupService = userGroupService;
		this.resolutionService = resolutionService;
		this.groupRepository = groupRepository;
		this.rolesRepository = rolesRepository;
		this.jsonMapper = jsonMapper;
	}

	// /api/groups GET -> Get all the groups that a user is participant
	public List<Group> getListOfGroups() {
		User user = authService.getAuthenticatedUser();
		return userGroupService.getGroups(user);
	}
	// /api/groups POST -> Create a new group (user will be admin)
	public Group createGroup(GroupCreationDTO groupCreationDTO) {
		User user = authService.getAuthenticatedUser();
		Group group = new Group();
		Optional<Roles> role = rolesRepository.findByRole(RolesEnum.ADMIN.getDbValue());
		if (role.isEmpty()) {
			logger.error("[GROUP SERVICE] Personal checker --- Roles uncorrect linking");
			throw new RuntimeException();
		}
		group.setName(groupCreationDTO.name());
		group.setDescription(groupCreationDTO.description());
		group.setCapacity(1);
		group = groupRepository.save(group);
		userGroupService.createNewRelation(user, group, role.get());
		return group;
	}
	// /api/groups/{groupId} PATCH -> Update the group by id (user must be the admin). it may be the group name/description/capacity
	public Group updateGroupInfo(Long groupId, Map<String, Object> patchPayload) {
		User user = authService.getAuthenticatedUser();
		Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
		UserGroup userGroup = userGroupService.findRelationByIds(user.getId(), groupId);
		if (!userGroup.isAdmin())
			throw new InvalidGroupAdminException("You're not the group administrator");
		if (patchPayload.containsKey("id") || patchPayload.containsKey("capacity"))
			throw new BadRequestCustomException("BAD_REQUEST", "Id and Capacity cannot be modified");
		group = applyPatch(patchPayload, group);
		groupRepository.save(group);
		return group;
	}
	// /api/groups/{groupId} DELETE -> Delete the group by id (user must be the admin)
	public void deleteGroup(Long groupId) {
		User user = authService.getAuthenticatedUser();
		Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
		UserGroup userGroup = userGroupService.findRelationByIds(user.getId(), groupId);
		if (!userGroup.isAdmin())
			throw new InvalidGroupAdminException("You're not the group administrator");
		groupRepository.delete(group);
	}
	// /api/groups/{groupId} GET -> Get the group info + users (only the username) (user must be a participant of the group to have the list of the users) (public/private group??)
	public GroupDataDTO getGroupInfo(Long groupId) {
		User user = authService.getAuthenticatedUser();
		Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
		userGroupService.validateRelation(user.getId(), groupId);
		List<String> participants = userGroupService.getParticipants(group);
		return new GroupDataDTO(group.getName(),
								group.getDescription(),
								participants);
	}
	// /api/{groupId}/{userName} GET -> Get username + the resolutions of that user (user must be a participant of the groupId)
	public List<ResolutionGetDTO> getUserResolutions(Long groupId, String username) {
		User authUser = authService.getAuthenticatedUser();
		userGroupService.validateRelation(authUser.getId(), groupId);
		User user = userService.getUser(username);
		userGroupService.validateRelation(user.getId(), groupId);
		return resolutionService.getUserResolutions(user);
	}







	public Group joinGroup(Long groupId) {
		User user = authService.getAuthenticatedUser();
		Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
		Optional<Roles> role = rolesRepository.findByRole(RolesEnum.MEMBER.getDbValue());
		if (role.isEmpty()) {
			logger.error("[GROUP SERVICE] Personal checker --- Roles uncorrect linking");
			throw new RuntimeException();
		}
		if (group.isFull())
			throw new GroupFullCapacityException();
		group.incrementCapacity();
		userGroupService.createNewRelation(user, group, role.get());
		groupRepository.save(group);
		return group;
	}

	public void leaveGroup(Long groupId) {
		User user = authService.getAuthenticatedUser();
		Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
		UserGroup userGroup = userGroupService.findRelationByIds(user.getId(), group.getId());

		if (userGroup.isAdmin()) {
			if (group.getCapacity() > 1)
				throw new InvalidGroupAdminException("Can't delete the group if you're the administrator");
		}
		/*
			Decirle a Zino si le parece bien devolver error al ser admin para pedirle un cambio.
			Delete group if necessary, decirle a Zino si lo quiere handlear el mandandome un /delete o si me encargo yo de una
		*/
		group.decrementCapacity();
		userGroupService.deleteRelation(userGroup);
		groupRepository.save(group);
	}














	private Group applyPatch(Map<String, Object> patchPayload, Group group) {
		ObjectNode newNode = jsonMapper.convertValue(group, ObjectNode.class);
		ObjectNode patchNode = jsonMapper.convertValue(patchPayload, ObjectNode.class);
		newNode.setAll(patchNode);
		return jsonMapper.convertValue(newNode, Group.class);
	}
}
