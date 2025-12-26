package com.proposito365.app.domain.user_group;

import com.proposito365.app.domain.groups.domain.Group;
import com.proposito365.app.domain.roles.Roles;
import com.proposito365.app.domain.roles.RolesEnum;
import com.proposito365.app.domain.users.domain.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_group")
public class UserGroup {
	@EmbeddedId
	private UserGroupId userGroupId;

	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@MapsId("groupId")
	@JoinColumn(name = "group_id")
	private Group group;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Roles role;

	public UserGroup() {}

	public UserGroup(User user, Group group, Roles role) {
		this.userGroupId = new UserGroupId(user.getId(), group.getId());
		this.user = user;
		this.group = group;
		this.role = role;
	}

	public UserGroupId getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(UserGroupId userGroupId) {
		this.userGroupId = userGroupId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	public boolean isAdmin() {
		if (role.getRole() == RolesEnum.ADMIN.getDbValue())
				return true;
		return false;
	}
}
