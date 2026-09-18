package com.company.travelplanner.travel.policy.repository;

import com.company.travelplanner.travel.policy.entity.PolicyViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyViolationRepository extends JpaRepository<PolicyViolation, Long> {
}
