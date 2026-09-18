package com.company.travelplanner.validator;

import com.company.travelplanner.entity.PolicyRule;
import com.company.travelplanner.entity.PolicyViolation;
import com.company.travelplanner.entity.TravelRequest;
import java.util.Optional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class AdvanceBookingValidator implements PolicyRuleValidator {
    @Override
    public boolean supports(PolicyRule rule) {
        return rule.getRuleType() == com.company.travelplanner.common.enums.RuleType.ADVANCE_BOOKING;
    }

    @Override
    public Optional<PolicyViolation> validate(TravelRequest request, PolicyRule rule) {
        long requiredDays;
        try {
            requiredDays = Long.parseLong(rule.getRuleValue());
        } catch (NumberFormatException exception) {
            return Optional.of(violation(rule, null, rule.getRuleValue(), "Invalid advance-booking rule value"));
        }
        long actualDays = ChronoUnit.DAYS.between(LocalDate.now(), request.getDepartureDate());
        if (actualDays >= requiredDays) {
            return Optional.empty();
        }
        return Optional.of(violation(rule, String.valueOf(actualDays), String.valueOf(requiredDays),
            "Travel must be booked further in advance"));
    }

    private PolicyViolation violation(PolicyRule rule, String actualValue, String allowedValue, String message) {
        PolicyViolation violation = new PolicyViolation();
        violation.setPolicyRule(rule);
        violation.setViolationType("ADVANCE_BOOKING");
        violation.setActualValue(actualValue);
        violation.setAllowedValue(allowedValue);
        violation.setMessage(message);
        violation.setSeverity(rule.getSeverity());
        return violation;
    }
}
