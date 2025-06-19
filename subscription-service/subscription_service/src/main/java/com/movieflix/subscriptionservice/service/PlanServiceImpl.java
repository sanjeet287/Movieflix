package com.movieflix.subscriptionservice.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.movieflix.subscriptionservice.dto.PlanDTO;
import com.movieflix.subscriptionservice.entity.DeviceType;
import com.movieflix.subscriptionservice.entity.Plan;
import com.movieflix.subscriptionservice.entity.PlanName;
import com.movieflix.subscriptionservice.exception.ResourceNotFoundException;
import com.movieflix.subscriptionservice.repository.PlanRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanServiceImpl implements PlanService {

	private final PlanRepository planRepository;

	@Override
	public List<PlanDTO> getAllPlans() {
		log.info("Fetching all plans");
		return planRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	@Override
	public PlanDTO getPlanByName(PlanName name) {
		log.info("Fetching plan by name: {}", name);
		Plan plan= planRepository.findByName(name)
				.orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + name));
	return toDTO(plan);
	}

	@PostConstruct
	public void initDefaultPlans() {
		if (planRepository.count() == 0) {
			log.info("Seeding default plans");
			List<Plan> plans = List.of(
					Plan.builder().name(PlanName.BASIC).price(199.0).durationInDays(30).maxDevices(1)
							.allowedDeviceTypes(Set.of(DeviceType.MOBILE)).build(),
					Plan.builder().name(PlanName.STANDARD).price(399.0).durationInDays(30).maxDevices(2)
							.allowedDeviceTypes(Set.of(DeviceType.MOBILE, DeviceType.LAPTOP)).build(),
					Plan.builder().name(PlanName.PREMIUM).price(699.0).durationInDays(30).maxDevices(4)
							.allowedDeviceTypes(
									Set.of(DeviceType.MOBILE, DeviceType.LAPTOP, DeviceType.TV, DeviceType.TABLET))
							.build());
			planRepository.saveAll(plans);
		}
	}

	@Override
	public PlanDTO createPlan(PlanDTO planDTO) {
		log.info("Creating new plan: {}", planDTO.getName());
		Plan plan = toEntity(planDTO);
		Plan saved = planRepository.save(plan);
		return toDTO(saved);
	}

	@Override
	public PlanDTO updatePlan(Long id, PlanDTO planDTO) {
		log.info("Updating plan with ID: {}", id);
		Plan existing = planRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + id));

		existing.setPrice(planDTO.getPrice());
		existing.setDurationInDays(planDTO.getDurationInDays());
		existing.setMaxDevices(planDTO.getMaxDevices());
		existing.setAllowedDeviceTypes(planDTO.getAllowedDeviceTypes());

		return toDTO(planRepository.save(existing));
	}

	@Override
	public void deletePlan(Long id) {
		log.info("Deleting plan with ID: {}", id);
		planRepository.deleteById(id);
	}

	private PlanDTO toDTO(Plan plan) {
		return PlanDTO.builder().id(plan.getId()).name(plan.getName()).price(plan.getPrice())
				.durationInDays(plan.getDurationInDays()).maxDevices(plan.getMaxDevices())
				.allowedDeviceTypes(plan.getAllowedDeviceTypes()).build();
	}

	private Plan toEntity(PlanDTO dto) {
		return Plan.builder().name(dto.getName()).price(dto.getPrice()).durationInDays(dto.getDurationInDays())
				.maxDevices(dto.getMaxDevices()).allowedDeviceTypes(dto.getAllowedDeviceTypes()).build();
	}

	@Override
	public PlanDTO getPlanById(Long id) {
	    log.info("Fetching plan by ID: {}", id);
	    Plan plan= planRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Plan not found with ID: " + id));
	return toDTO(plan);
	}

}
