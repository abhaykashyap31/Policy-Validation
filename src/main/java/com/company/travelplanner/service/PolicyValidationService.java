package com.company.travelplanner.service;

import com.company.travelplanner.common.enums.TravelRequestStatus;
import com.company.travelplanner.common.enums.ValidationStatus;
import com.company.travelplanner.common.exception.PolicyValidationException;
import com.company.travelplanner.common.exception.ResourceNotFoundException;
import com.company.travelplanner.dto.PolicyValidationResponse;
import com.company.travelplanner.dto.PolicyViolationResponse;
import com.company.travelplanner.entity.PolicyRule;
import com.company.travelplanner.entity.PolicyValidation;
import com.company.travelplanner.entity.PolicyViolation;
import com.company.travelplanner.entity.TravelPolicy;
import com.company.travelplanner.repository.PolicyValidationRepository;
import com.company.travelplanner.repository.TravelPolicyRepository;
import com.company.travelplanner.validator.PolicyRuleValidator;
import com.company.travelplanner.entity.TravelRequest;
import com.company.travelplanner.repository.TravelRequestRepository;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyValidationService {

    private final TravelRequestRepository travelRequestRepository;
    private final TravelPolicyRepository travelPolicyRepository;
    private final PolicyValidationRepository policyValidationRepository;
    private final List<PolicyRuleValidator> validators;

    public PolicyValidationService(TravelRequestRepository travelRequestRepository,
                                   TravelPolicyRepository travelPolicyRepository,
                                   PolicyValidationRepository policyValidationRepository,
                                   List<PolicyRuleValidator> validators) {
        this.travelRequestRepository = travelRequestRepository;
        this.travelPolicyRepository = travelPolicyRepository;
        this.policyValidationRepository = policyValidationRepository;
        this.validators = validators;
    }

    @Transactional
    public PolicyValidationResponse validate(Long travelRequestId) {
        if (travelRequestId == null) {
            throw new PolicyValidationException("Travel request id is required");
        }
        TravelRequest request = travelRequestRepository.findById(travelRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found: " + travelRequestId));
        if (request.getEmployee().getGrade() == null) {
            throw new PolicyValidationException("Employee grade is required to select a policy");
        }
        TravelPolicy policy = travelPolicyRepository.findFirstByGradeAndActiveTrue(request.getEmployee().getGrade())
                .orElseThrow(() -> new ResourceNotFoundException("Active policy not found for grade: " + request.getEmployee().getGrade()));
        LocalDate today = LocalDate.now();
        if (policy.getEffectiveFrom() != null && today.isBefore(policy.getEffectiveFrom())
            || policy.getEffectiveTo() != null && today.isAfter(policy.getEffectiveTo())) {
            throw new PolicyValidationException("The active policy is outside its effective date range");
        }

        PolicyValidation validation = new PolicyValidation();
        validation.setTravelRequest(request);
        validation.setPolicy(policy);
        validation.setValidatedAt(LocalDateTime.now());

        for (PolicyRule rule : policy.getRules()) {
            if (!rule.isActive()) {
                continue;
            }
            Optional<PolicyRuleValidator> validator = validators.stream()
                    .filter(candidate -> candidate.supports(rule))
                    .findFirst();
            if (validator.isPresent()) {
                validator.get().validate(request, rule).ifPresent(violation -> {
                    violation.setValidation(validation);
                    validation.getViolations().add(violation);
                });
            }
        }

        validation.setOverallStatus(validation.getViolations().isEmpty()
                ? ValidationStatus.PASSED
                : ValidationStatus.FAILED);
        request.setStatus(validation.getViolations().isEmpty()
                ? TravelRequestStatus.PENDING_APPROVAL
                : TravelRequestStatus.FLAGGED);
        PolicyValidation saved = policyValidationRepository.save(validation);
        return toResponse(saved);
    }

    private PolicyValidationResponse toResponse(PolicyValidation validation) {
        List<PolicyViolationResponse> violations = validation.getViolations().stream()
                .map(this::toResponse)
                .toList();
        return new PolicyValidationResponse(
                validation.getId(),
                validation.getTravelRequest().getId(),
                validation.getPolicy().getId(),
                validation.getOverallStatus(),
                validation.getValidatedAt(),
                violations);
    }

    private PolicyViolationResponse toResponse(PolicyViolation violation) {
        return new PolicyViolationResponse(
                violation.getPolicyRule().getRuleCode(),
                violation.getMessage(),
                violation.getActualValue(),
                violation.getAllowedValue(),
                violation.getSeverity());
    }
}
