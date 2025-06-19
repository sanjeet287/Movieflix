package com.movieflix.subscriptionservice.controller;

import com.movieflix.subscriptionservice.dto.PlanDTO;
import com.movieflix.subscriptionservice.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
@Slf4j
public class PlanController {

	private final PlanService planService;
	
	@GetMapping("/all")
	public ResponseEntity<List<PlanDTO>> getAllPlans() {
		log.info("Request to fetch all plans");
		log.info("Request to fetch all plans");
		List<PlanDTO> plans = planService.getAllPlans();
		return ResponseEntity.ok(plans);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlanDTO> getPlanById(@PathVariable Long id) {
		log.info("Request to fetch plan with id: {}", id);
		PlanDTO planDTO = toDTO(planService.getPlanById(id));
		return ResponseEntity.ok(planDTO);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/new")
	public ResponseEntity<PlanDTO> createPlan(@RequestBody PlanDTO planDTO) {
		log.info("Request to create new plan: {}", planDTO.getName());
		PlanDTO createdPlan = toDTO(planService.createPlan(planDTO));
		return ResponseEntity.ok(createdPlan);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<PlanDTO> updatePlan(@PathVariable Long id, @RequestBody PlanDTO planDTO) {
		log.info("Request to update plan id: {}", id);
		PlanDTO updatedPlan = planService.updatePlan(id, planDTO);
		return ResponseEntity.ok(updatedPlan);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
		log.info("Request to delete plan id: {}", id);
		planService.deletePlan(id);
		return ResponseEntity.noContent().build();
	}

	private PlanDTO toDTO(PlanDTO planDTO) {
		return PlanDTO.builder().id(planDTO.getId()).name(planDTO.getName()).price(planDTO.getPrice())
				.durationInDays(planDTO.getDurationInDays()).maxDevices(planDTO.getMaxDevices())
				.allowedDeviceTypes(planDTO.getAllowedDeviceTypes()).build();
	}
}
