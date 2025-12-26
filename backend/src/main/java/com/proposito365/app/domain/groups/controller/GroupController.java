package com.proposito365.app.domain.groups.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.domain.groups.domain.Group;
import com.proposito365.app.domain.groups.domain.GroupCreationDTO;
import com.proposito365.app.domain.groups.service.GroupService;

/*	- /api/groups GET -> Get all the groups that a user is participant
	- /api/groups POST -> Create a new group (user will be admin)
	- /api/groups/{groupId} PATCH -> Update the group by id (user must be the admin). it may be the group name/description/capacity
	- /api/groups/{groupId} DELETE -> Delete the group by id (user must be the admin)

	- /api/groups/{groupId} GET -> Get the group info + users (only the username and resolutions!) (user must be a participant of the group to have the list of the users) (public/private group??)
	- /api/{groupId}/{userName} GET -> Get username + the resolutions of that user (user must be a participant of the groupId) */

@RestController
@RequestMapping("/api/groups")
public class GroupController {
	private GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping
	public ResponseEntity<List<Group>> getGroups() {
		List<Group> groups = groupService.getListOfGroups();
		return ResponseEntity.ok(groups);
	}

	@PostMapping
	public ResponseEntity<Group> createGroup(@RequestBody GroupCreationDTO groupCreationDTO) {
		Group response = groupService.createGroup(groupCreationDTO);
		return ResponseEntity.ok(response);
	}

	/*add the other endpoints!!!! */

	@PatchMapping("/{groupId}")
	public ResponseEntity<Group> updateGroupInfo(@PathVariable Long groupId, Map<String, Object> patchPayload) {
		Group response = groupService.updateGroupInfo(groupId, patchPayload);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{groupId}/join")
	public ResponseEntity<Group> joinGroup(@PathVariable Long groupId) {
		Group group = groupService.joinGroup(groupId);
		return ResponseEntity.ok(group);
	}

	@PostMapping("/{groupId}/leave")
	public ResponseEntity<Void> leaveGroup(@PathVariable Long groupId) {
		groupService.leaveGroup(groupId);
		return ResponseEntity.ok().build();
	}

}
