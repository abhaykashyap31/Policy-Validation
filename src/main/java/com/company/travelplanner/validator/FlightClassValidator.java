package com.company.travelplanner.validator;

import com.company.travelplanner.entity.PolicyRule;
import com.company.travelplanner.entity.PolicyViolation;
import com.company.travelplanner.entity.TravelRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class FlightClassValidator implements PolicyRuleValidator {

    @Override
    public boolean supports(PolicyRule rule) {
        return rule.getRuleType() == com.company.travelplanner.common.enums.RuleType.FLIGHT_CLASS;
    }

    @Override
    public Optional<PolicyViolation> validate(TravelRequest request, PolicyRule rule) {
        String actualValue = request.getTravelClass() == null ? null : request.getTravelClass().name();
        if (actualValue != null && rule.getRuleValue() != null
            && actualValue.equalsIgnoreCase(rule.getRuleValue())) {
            return Optional.empty();
        }
        PolicyViolation violation = new PolicyViolation();
        violation.setPolicyRule(rule);
        violation.setViolationType("FLIGHT_CLASS");
        violation.setMessage("Travel class is not permitted by policy");
        violation.setActualValue(actualValue);
        violation.setAllowedValue(rule.getRuleValue());
        violation.setSeverity(rule.getSeverity());
        return Optional.of(violation);
    }
}
