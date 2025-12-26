package com.proposito365.app.domain.user_group;

import java.util.List;

import com.proposito365.app.domain.groups.domain.Group;
import com.proposito365.app.domain.roles.Roles;
import com.proposito365.app.domain.users.domain.User;

public interface UserGroupService {
	UserGroup findRelationByIds(Long userId, Long groupId);
	void createNewRelation(User user, Group group, Roles role);
	void deleteRelation(UserGroup userGroup);
	List<Group> getGroups(User user);
	List<String> getParticipants(Group group);
	void validateRelation(Long userId, Long groupId);
	UserGroup changeRoleToAdmin(UserGroup userGroup);
	UserGroup changeRoleToMember(UserGroup userGroup);
}
