package com.proposito365.app.domain.user_group;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import com.proposito365.app.common.exceptions.groups.UserGroupRelationException;
import com.proposito365.app.domain.groups.domain.Group;
import com.proposito365.app.domain.roles.Roles;
import com.proposito365.app.domain.roles.RolesEnum;
import com.proposito365.app.domain.roles.RolesRepository;
import com.proposito365.app.domain.users.domain.User;

@Service
public class UserGroupServiceImpl implements UserGroupService {
	private final static Logger logger = Logger.getLogger(UserGroupServiceImpl.class);
	private UserGroupRepository userGroupRepository;
	private RolesRepository rolesRepository;

	public UserGroupServiceImpl(UserGroupRepository userGroupRepository,
								RolesRepository rolesRepository) {
		this.userGroupRepository = userGroupRepository;
		this.rolesRepository = rolesRepository;
	}

	public UserGroup findRelationByIds(Long userId, Long groupId) {
		UserGroupId userGroupId = new UserGroupId(userId, groupId);
		return userGroupRepository.findById(userGroupId).orElseThrow(UserGroupRelationException::new);
	}

	public void createNewRelation(User user, Group group, Roles role) {
		UserGroup userGroup = new UserGroup(user, group, role);
		userGroupRepository.save(userGroup);
	}

	public void deleteRelation(UserGroup userGroup) {
		userGroupRepository.delete(userGroup);
	}

	public List<Group> getGroups(User user) {
		return user.getGroups()
				   .stream()
				   .map(UserGroup::getGroup)
				   .collect(Collectors.toList());
	}

	public List<String> getParticipants(Group group) {
		List<String> participants = new ArrayList<>();
		for (UserGroup ug : group.getUsers()) {
			participants.add(ug.getUser().getUsername());
		}
		return participants;
	}

	public void validateRelation(Long userId, Long groupId) {
		this.findRelationByIds(userId, groupId);
	}

	public UserGroup changeRoleToAdmin(UserGroup userGroup) {
		Optional<Roles> role = rolesRepository.findByRole(RolesEnum.ADMIN.getDbValue());
		if (role.isEmpty()) {
			logger.error("[USERGROUP SERVICE] Personal checker --- Roles uncorrect linking");
			throw new RuntimeException();
		}
		userGroup.setRole(role.get());
		userGroupRepository.save(userGroup);
		return userGroup;
	}

	public UserGroup changeRoleToMember(UserGroup userGroup) {
		Optional<Roles> role = rolesRepository.findByRole(RolesEnum.MEMBER.getDbValue());
		if (role.isEmpty()) {
			logger.error("[USERGROUP SERVICE] Personal checker --- Roles uncorrect linking");
			throw new RuntimeException();
		}
		userGroup.setRole(role.get());
		userGroupRepository.save(userGroup);
		return userGroup;
	}
}
