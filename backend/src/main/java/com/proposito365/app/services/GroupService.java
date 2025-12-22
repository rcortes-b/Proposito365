package com.proposito365.app.services;

import java.util.Optional;

import com.proposito365.app.models.Group;
import com.proposito365.app.repository.GroupRepository;

public class GroupService {
	private GroupRepository groupRepository;

	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	public Group getGroupById(Long id) {
		Optional<Group> group = groupRepository.findById(id);
		if (group.isEmpty())
			return null;
		return group.get();
	}
}
