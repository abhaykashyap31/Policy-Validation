package com.company.travelplanner.travel.policy.validator;

import com.company.travelplanner.travel.policy.entity.PolicyRule;
import com.company.travelplanner.travel.policy.entity.PolicyViolation;
import com.company.travelplanner.travel.request.entity.TravelRequest;
import java.util.Optional;

public interface PolicyRuleValidator {
    boolean supports(PolicyRule rule);
    Optional<PolicyViolation> validate(TravelRequest request, PolicyRule rule);
}
