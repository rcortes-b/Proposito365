package com.proposito365.app.services;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import com.proposito365.app.controller.ResolutionController;
import com.proposito365.app.domain.ResolutionGetDTO;
import com.proposito365.app.domain.ResolutionPostDTO;
import com.proposito365.app.domain.StatusEnum;
import com.proposito365.app.models.Resolution;
import com.proposito365.app.models.Status;
import com.proposito365.app.models.User;
import com.proposito365.app.repository.ResolutionRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

/*
	- /api/resolutions GET -> Get the resolution of one concrete user
	- /api/resolutions POST -> Create a resolution of one concrete user
	- /api/resolutions/{resolutionId} PATCH -> Patch a resolution of one concrete user by its ID (user id must match with the user_id in the users_resolution table)
	- /api/resolutions/{resolutionId} DELETE -> Delete a resolution of one concrete user by its ID (user id must match with the user_id in the users_resolution table)
*/

	/*	Doing the lookup in the user.getResolutions instead of resolutionRepository.findById()
		I only check the resolutions of the user itself
	*/

@Service
@Transactional
public class ResolutionService {
	public final static Logger logger = Logger.getLogger(ResolutionController.class);
	private ResolutionRepository resolutionRepository;
	private UserService userService;
	private JsonMapper jsonMapper;

	public ResolutionService(ResolutionRepository resolutionRepository, UserService userService, JsonMapper jsonMapper) {
		this.resolutionRepository = resolutionRepository;
		this.userService = userService;
		this.jsonMapper = jsonMapper;
	}

	public List<ResolutionGetDTO> getUserResolutions(Principal login) {
		User user = userService.getUserByUsername(login.getName());
		if (user == null)
			return null;
		List<ResolutionGetDTO> resolutions = user.getResolutions().stream()
																  .map(r -> new ResolutionGetDTO(
																	r.getId(),
																	r.getResolution(),
																	r.getDetails(),
																	r.getStatus().getValue()
																  ))
																  .collect(Collectors.toList());
		return resolutions;					
	}

	public Resolution createResolution(Principal login, ResolutionPostDTO resolutionDTO) {
		User user = userService.getUserByUsername(login.getName());
		if (user == null)
			return null;
		Status status = new Status();
		status.setId(StatusEnum.IN_PROGRESS.getId());
		Resolution resolution = new Resolution(user, resolutionDTO.resolution(),
								resolutionDTO.details(), status);
		return resolutionRepository.save(resolution);
	}

	public Resolution patchResolution(Long resolutionId, Map<String, Object> patchPayload, Principal login) {
		User user = userService.getUserByUsername(login.getName());
		if (user == null)
			return null;
		if (patchPayload.containsKey("id") || patchPayload.containsKey("user_id"))
			return null;
		Resolution patchedResolution = null;
		for (Resolution r : user.getResolutions()) {
			if (r.getId() == resolutionId)
			{
				patchedResolution = applyPatch(patchPayload, r);
				patchedResolution.setUser(user);
				break;
			}
		}
		if (patchedResolution != null)
			resolutionRepository.save(patchedResolution);
		return patchedResolution;
	}

	public Resolution deleteResolution(Long resolutionId, Principal login) {
		User user = userService.getUserByUsername(login.getName());
		if (user == null)
			return null;
		Resolution resolution = null;
		for (Resolution r : user.getResolutions()) {
			if ( r.getId() == resolutionId)
			{
				resolution = r;
				break;
			}
		}
		if (resolution != null)
			resolutionRepository.delete(resolution);
		return resolution;
	}

	private Resolution applyPatch(Map<String, Object> patchPayload, Resolution resolution) {
		ObjectNode newResolution = jsonMapper.convertValue(resolution, ObjectNode.class);
		ObjectNode patchNode = jsonMapper.convertValue(patchPayload, ObjectNode.class);
		
		newResolution.setAll(patchNode);

		return jsonMapper.convertValue(newResolution, Resolution.class);
	}
}
