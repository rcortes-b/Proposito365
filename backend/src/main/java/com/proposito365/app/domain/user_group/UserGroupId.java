package com.proposito365.app.domain.user_group;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserGroupId implements Serializable {
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "group_id")
	private Long groupId;

	public UserGroupId() {};

	public UserGroupId(Long userId, Long groupId) {
		this.userId = userId;
		this.groupId = groupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, groupId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		UserGroupId other = (UserGroupId) obj;
		return (Objects.equals(other.userId, this.userId) && 
				Objects.equals(other.groupId, this.groupId));
	}
	
	


}
