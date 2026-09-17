package com.company.travelplanner.policy.validator;

import com.company.travelplanner.policy.entity.PolicyRule;
import com.company.travelplanner.policy.entity.PolicyViolation;
import com.company.travelplanner.travelrequest.entity.TravelRequest;
import java.util.Optional;

public interface PolicyRuleValidator {
    boolean supports(PolicyRule rule);
    Optional<PolicyViolation> validate(TravelRequest request, PolicyRule rule);
}
