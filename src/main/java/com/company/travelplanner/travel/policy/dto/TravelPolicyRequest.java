package com.company.travelplanner.travel.policy.dto;

import java.time.LocalDate;
import java.util.List;

public record TravelPolicyRequest(
        String name,
        String description,
        String policyType,
        Integer version,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        String grade,
        Boolean active,
        List<PolicyRuleRequest> rules) {
}
