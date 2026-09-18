package com.company.travelplanner.travel.policy.validator;

import com.company.travelplanner.travel.policy.entity.PolicyRule;
import com.company.travelplanner.travel.policy.entity.PolicyViolation;
import com.company.travelplanner.common.enums.RuleType;
import com.company.travelplanner.travel.request.entity.TravelRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TravelModeValidator implements PolicyRuleValidator {

    @Override
    public boolean supports(PolicyRule rule) {
        return rule.getRuleType() == RuleType.TRAVEL_MODE;
    }

    @Override
    public Optional<PolicyViolation> validate(TravelRequest request, PolicyRule rule) {
        String actualValue = request.getTravelMode() == null ? null : request.getTravelMode().name();
        if (actualValue != null && actualValue.equalsIgnoreCase(rule.getRuleValue())) {
            return Optional.empty();
        }
        PolicyViolation violation = new PolicyViolation();
        violation.setPolicyRule(rule);
        violation.setViolationType("TRAVEL_MODE");
        violation.setMessage("Travel mode is not permitted by policy");
        violation.setActualValue(actualValue);
        violation.setAllowedValue(rule.getRuleValue());
        violation.setSeverity(rule.getSeverity());
        return Optional.of(violation);
    }
}