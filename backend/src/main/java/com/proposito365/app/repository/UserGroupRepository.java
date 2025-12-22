package com.proposito365.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.Group;
import com.proposito365.app.models.User;
import com.proposito365.app.models.UserGroup;
import com.proposito365.app.models.UserGroupId;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId>  {
	Optional<UserGroup> findByUserAndGroup(User user, Group group);
}
