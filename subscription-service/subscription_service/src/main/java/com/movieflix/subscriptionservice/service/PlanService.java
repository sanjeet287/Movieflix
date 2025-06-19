package com.movieflix.subscriptionservice.service;

import com.movieflix.subscriptionservice.dto.PlanDTO;
import com.movieflix.subscriptionservice.entity.PlanName;

import java.util.List;

public interface PlanService {

    List<PlanDTO> getAllPlans();
    
    PlanDTO getPlanById(Long id);

    PlanDTO getPlanByName(PlanName name);

    void initDefaultPlans();

    PlanDTO createPlan(PlanDTO planDTO);

    PlanDTO updatePlan(Long id, PlanDTO planDTO);

    void deletePlan(Long id);
}
