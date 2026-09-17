package com.company.travelplanner.policy.repository;

import com.company.travelplanner.policy.entity.PolicyViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyViolationRepository extends JpaRepository<PolicyViolation, Long> {
}
