package com.company.travelplanner.travel.policy.repository;

import com.company.travelplanner.travel.policy.entity.PolicyRule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRuleRepository extends JpaRepository<PolicyRule, Long> {
    List<PolicyRule> findByPolicyId(Long policyId);
}
