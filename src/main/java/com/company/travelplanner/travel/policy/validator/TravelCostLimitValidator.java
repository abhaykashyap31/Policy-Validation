package com.company.travelplanner.travel.policy.validator;

import com.company.travelplanner.common.enums.RuleType;
import com.company.travelplanner.travel.policy.entity.PolicyRule;
import com.company.travelplanner.travel.policy.entity.PolicyViolation;
import com.company.travelplanner.travel.request.entity.TravelRequest;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TravelCostLimitValidator implements PolicyRuleValidator {

    @Override
    public boolean supports(PolicyRule rule) {
        return rule.getRuleType() == RuleType.TRAVEL_COST_LIMIT;
    }

    @Override
    public Optional<PolicyViolation> validate(TravelRequest request, PolicyRule rule) {
        if (request.getEstimatedCost() == null) {
            return Optional.empty();
        }
        try {
            BigDecimal limit = new BigDecimal(rule.getRuleValue());
            if (request.getEstimatedCost().compareTo(limit) <= 0) {
                return Optional.empty();
            }
        } catch (NumberFormatException exception) {
            // An invalid policy value is reported as a policy violation.
        }
        PolicyViolation violation = new PolicyViolation();
        violation.setPolicyRule(rule);
        violation.setViolationType("TRAVEL_COST_LIMIT");
        violation.setMessage("Estimated travel cost exceeds the policy limit");
        violation.setActualValue(request.getEstimatedCost().toPlainString());
        violation.setAllowedValue(rule.getRuleValue());
        violation.setSeverity(rule.getSeverity());
        return Optional.of(violation);
    }
}