package com.company.travelplanner.validator;

import com.company.travelplanner.entity.PolicyRule;
import com.company.travelplanner.entity.PolicyViolation;
import com.company.travelplanner.entity.TravelRequest;
import java.util.Optional;

public interface PolicyRuleValidator {
    boolean supports(PolicyRule rule);
    Optional<PolicyViolation> validate(TravelRequest request, PolicyRule rule);
}
