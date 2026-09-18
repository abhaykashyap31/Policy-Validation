package com.company.travelplanner.repository;

import com.company.travelplanner.entity.PolicyViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyViolationRepository extends JpaRepository<PolicyViolation, Long> {
}
