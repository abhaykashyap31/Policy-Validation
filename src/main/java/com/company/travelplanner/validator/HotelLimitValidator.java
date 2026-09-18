package com.company.travelplanner.validator;

import com.company.travelplanner.entity.PolicyRule;
import com.company.travelplanner.entity.PolicyViolation;
import com.company.travelplanner.entity.TravelRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class HotelLimitValidator implements PolicyRuleValidator {
    @Override
    public boolean supports(PolicyRule rule) {
        return rule.getRuleType() == com.company.travelplanner.common.enums.RuleType.HOTEL_LIMIT;
    }

    @Override
    public Optional<PolicyViolation> validate(TravelRequest request, PolicyRule rule) {
        if (request.getEstimatedCost() == null) {
            return Optional.empty();
        }
        try {
            java.math.BigDecimal limit = new java.math.BigDecimal(rule.getRuleValue());
            if (request.getEstimatedCost().compareTo(limit) <= 0) {
                return Optional.empty();
            }
        } catch (NumberFormatException exception) {
            // Treat an invalid policy value as a policy violation below.
        }
        PolicyViolation violation = new PolicyViolation();
        violation.setPolicyRule(rule);
        violation.setViolationType("HOTEL_LIMIT");
        violation.setMessage("Estimated cost exceeds the policy limit");
        violation.setActualValue(request.getEstimatedCost().toPlainString());
        violation.setAllowedValue(rule.getRuleValue());
        violation.setSeverity(rule.getSeverity());
        return Optional.of(violation);
    }
}
