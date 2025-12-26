package com.proposito365.app.domain.resolutions.controller;

import org.springframework.web.bind.annotation.RestController;

import com.proposito365.app.domain.resolutions.domain.Resolution;
import com.proposito365.app.domain.resolutions.domain.ResolutionGetDTO;
import com.proposito365.app.domain.resolutions.domain.ResolutionPostDTO;
import com.proposito365.app.domain.resolutions.service.ResolutionService;
import com.proposito365.app.infrastructure.middleware.auth.AuthService;

import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.http.ResponseEntity;
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
	private AuthService authService;
	private ResolutionService resolutionService;

	public ResolutionController(AuthService authService, ResolutionService resolutionService) {
		this.authService = authService;
		this.resolutionService = resolutionService;
	}
	
	@GetMapping
	public ResponseEntity<List<ResolutionGetDTO>> getUserResolutions() {
		List<ResolutionGetDTO> response = resolutionService.getUserResolutions(authService.getAuthenticatedUser());
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<Void> createResolution(@RequestBody ResolutionPostDTO resolutionDTO) {
		resolutionService.createResolution(resolutionDTO);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("{resolutionId}")
	public ResponseEntity<Resolution> patchResolution(@PathVariable Long resolutionId,
													  @RequestBody Map<String, Object> patchPayload) {
		Resolution response = resolutionService.patchResolution(resolutionId, patchPayload);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("{resolutionId}")
	public ResponseEntity<Void> deleteResolution(@PathVariable Long resolutionId) {
		resolutionService.deleteResolution(resolutionId);
		return ResponseEntity.ok().build();
	}
	
}
