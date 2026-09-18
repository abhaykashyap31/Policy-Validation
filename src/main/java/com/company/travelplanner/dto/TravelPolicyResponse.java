package com.company.travelplanner.dto;

import java.time.LocalDate;
import java.util.List;

public record TravelPolicyResponse(
        Long id,
        String name,
        String description,
        String policyType,
        Integer version,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        String grade,
        boolean active,
        List<PolicyRuleResponse> rules) {
}
