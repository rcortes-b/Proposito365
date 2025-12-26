package com.proposito365.app.domain.resolutions.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import com.proposito365.app.common.exceptions.BadRequestCustomException;
import com.proposito365.app.common.exceptions.ResolutionNotFoundException;
import com.proposito365.app.domain.resolutions.controller.ResolutionController;
import com.proposito365.app.domain.resolutions.domain.Resolution;
import com.proposito365.app.domain.resolutions.domain.ResolutionGetDTO;
import com.proposito365.app.domain.resolutions.domain.ResolutionPostDTO;
import com.proposito365.app.domain.resolutions.repository.ResolutionRepository;
import com.proposito365.app.domain.status.Status;
import com.proposito365.app.domain.status.StatusEnum;
import com.proposito365.app.domain.status.StatusRepository;
import com.proposito365.app.domain.users.domain.User;
import com.proposito365.app.infrastructure.middleware.auth.AuthService;

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
	private final static Logger logger = Logger.getLogger(ResolutionController.class);

	private AuthService authService;
	private ResolutionRepository resolutionRepository;
	private StatusRepository statusRepository;
	private JsonMapper jsonMapper;

	public ResolutionService(AuthService authService, ResolutionRepository resolutionRepository,
								StatusRepository statusRepository, JsonMapper jsonMapper) {
		this.authService = authService;
		this.resolutionRepository = resolutionRepository;
		this.statusRepository = statusRepository;
		this.jsonMapper = jsonMapper;
	}

	public List<ResolutionGetDTO> getUserResolutions(User user) {
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

	public List<ResolutionGetDTO> getUserResolutions() {
		User user = authService.getAuthenticatedUser();
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

	public void createResolution(ResolutionPostDTO resolutionDTO) {
		User user = authService.getAuthenticatedUser();
		Optional<Status> status = statusRepository.findByValue(StatusEnum.IN_PROGRESS.getDbValue());
		if (status.isEmpty()) {
			logger.error("[RESOLUTION SERVICE] Personal checker --- Status uncorrect linking");
			throw new RuntimeException();
		}
		Resolution resolution = new Resolution(user, resolutionDTO.resolution(),
								resolutionDTO.details(), status.get());
		resolutionRepository.save(resolution);
	}

	public Resolution patchResolution(Long resolutionId, Map<String, Object> patchPayload) {
		User user = authService.getAuthenticatedUser();
		if (patchPayload.containsKey("id") || patchPayload.containsKey("user_id"))
			throw new BadRequestCustomException("BAD_REQUEST", "Resolution and user ids cannot be modified");
		Resolution patchedResolution = null;
		for (Resolution r : user.getResolutions()) {
			if (r.getId() == resolutionId)
			{
				patchedResolution = applyPatch(patchPayload, r);
				patchedResolution.setUser(user);
				break;
			}
		}
		if (patchedResolution == null)
			throw new ResolutionNotFoundException();
		resolutionRepository.save(patchedResolution);
		return patchedResolution;
	}

	public void deleteResolution(Long resolutionId) {
		User user = authService.getAuthenticatedUser();
		Resolution resolution = null;
		for (Resolution r : user.getResolutions()) {
			if ( r.getId() == resolutionId)
			{
				resolution = r;
				break;
			}
		}
		if (resolution == null)
			throw new ResolutionNotFoundException();
		resolutionRepository.delete(resolution);
	}

	private Resolution applyPatch(Map<String, Object> patchPayload, Resolution resolution) {
		ObjectNode newResolution = jsonMapper.convertValue(resolution, ObjectNode.class);
		ObjectNode patchNode = jsonMapper.convertValue(patchPayload, ObjectNode.class);
		
		newResolution.setAll(patchNode);

		return jsonMapper.convertValue(newResolution, Resolution.class);
	}
}
