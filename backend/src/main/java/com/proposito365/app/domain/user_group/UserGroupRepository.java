package com.proposito365.app.domain.user_group;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.domain.groups.domain.Group;
import com.proposito365.app.domain.users.domain.User;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId>  {
	Optional<UserGroup> findByUserAndGroup(User user, Group group);
}
