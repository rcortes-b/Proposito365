package com.proposito365.app.controller;

import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.dto.ResolutionGetDTO;
import com.proposito365.app.dto.ResolutionPostDTO;
import com.proposito365.app.models.Resolution;
import com.proposito365.app.services.ResolutionService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/resolutions")
public class ResolutionController {
	public final static Logger logger = Logger.getLogger(ResolutionController.class);
	private ResolutionService resolutionService;

	public ResolutionController(ResolutionService resolutionService) {
		this.resolutionService = resolutionService;
	}
	
	@GetMapping
	public List<ResolutionGetDTO> getUserResolutions(Principal user) {
		return resolutionService.getUserResolutions(user);
	}

	@PostMapping
	public void createResolution(Principal user, @RequestBody ResolutionPostDTO resolutionDTO) {
		resolutionService.createResolution(user, resolutionDTO);
	}

	@PatchMapping("{resolutionId}")
	public Resolution patchResolution(@PathVariable Long resolutionId, @RequestBody Map<String, Object> patchPayload,
										Principal user) {
		return resolutionService.patchResolution(resolutionId, patchPayload, user);
	}

	@DeleteMapping("{resolutionId}")
	public void deleteResolution(@PathVariable Long resolutionId, Principal user) {
		resolutionService.deleteResolution(resolutionId, user);
	}
	
}
