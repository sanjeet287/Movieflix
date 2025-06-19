package com.movieflix.subscriptionservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieflix.subscriptionservice.entity.Plan;
import com.movieflix.subscriptionservice.entity.PlanName;

public interface PlanRepository extends JpaRepository<Plan, Long> {

	Optional<Plan> findByName(PlanName name);
}
