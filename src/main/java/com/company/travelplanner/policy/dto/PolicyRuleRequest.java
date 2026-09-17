package com.company.travelplanner.policy.dto;

import com.company.travelplanner.common.enums.RuleType;
import com.company.travelplanner.common.enums.Severity;

public record PolicyRuleRequest(
        String ruleCode,
        RuleType ruleType,
        String ruleValue,
        Severity severity,
        String description,
        Boolean active) {
}
