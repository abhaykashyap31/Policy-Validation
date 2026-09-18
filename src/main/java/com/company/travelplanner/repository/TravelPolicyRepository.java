package com.company.travelplanner.repository;

import com.company.travelplanner.entity.TravelPolicy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPolicyRepository extends JpaRepository<TravelPolicy, Long> {
    Optional<TravelPolicy> findFirstByGradeAndActiveTrue(String grade);
}
