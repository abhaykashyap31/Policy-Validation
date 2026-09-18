package com.company.travelplanner.travel.policy.dto;

import com.company.travelplanner.common.enums.RuleType;
import com.company.travelplanner.common.enums.Severity;

public record PolicyRuleResponse(
        Long id,
        String ruleCode,
        RuleType ruleType,
        String ruleValue,
        Severity severity,
        String description,
        boolean active) {
}
