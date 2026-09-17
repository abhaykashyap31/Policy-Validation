package com.company.travelplanner.policy.service;

import com.company.travelplanner.common.exception.PolicyValidationException;
import com.company.travelplanner.common.exception.ResourceNotFoundException;
import com.company.travelplanner.policy.dto.PolicyRuleRequest;
import com.company.travelplanner.policy.dto.PolicyRuleResponse;
import com.company.travelplanner.policy.dto.TravelPolicyRequest;
import com.company.travelplanner.policy.dto.TravelPolicyResponse;
import com.company.travelplanner.policy.entity.PolicyRule;
import com.company.travelplanner.policy.entity.TravelPolicy;
import com.company.travelplanner.policy.repository.TravelPolicyRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TravelPolicyService {

    private final TravelPolicyRepository travelPolicyRepository;

    public TravelPolicyService(TravelPolicyRepository travelPolicyRepository) {
        this.travelPolicyRepository = travelPolicyRepository;
    }

    @Transactional
    public TravelPolicyResponse createPolicy(TravelPolicyRequest request) {
        validate(request);
        TravelPolicy policy = new TravelPolicy();
        policy.setName(request.name());
        policy.setDescription(request.description());
        policy.setPolicyType(request.policyType());
        policy.setVersion(request.version() == null ? 1 : request.version());
        policy.setEffectiveFrom(request.effectiveFrom());
        policy.setEffectiveTo(request.effectiveTo());
        policy.setGrade(request.grade());
        policy.setActive(request.active() == null || request.active());

        if (request.rules() != null) {
            for (PolicyRuleRequest ruleRequest : request.rules()) {
                PolicyRule rule = new PolicyRule();
                rule.setPolicy(policy);
                rule.setRuleCode(ruleRequest.ruleCode());
                rule.setRuleType(ruleRequest.ruleType());
                rule.setRuleValue(ruleRequest.ruleValue());
                rule.setSeverity(ruleRequest.severity());
                rule.setDescription(ruleRequest.description());
                rule.setActive(ruleRequest.active() == null || ruleRequest.active());
                policy.getRules().add(rule);
            }
        }
        return toResponse(travelPolicyRepository.save(policy));
    }

    @Transactional(readOnly = true)
    public TravelPolicyResponse getPolicy(Long id) {
        TravelPolicy policy = travelPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel policy not found: " + id));
        return toResponse(policy);
    }

    private void validate(TravelPolicyRequest request) {
        if (request == null || isBlank(request.name()) || isBlank(request.grade())) {
            throw new PolicyValidationException("Policy name and grade are required");
        }
        if (request.version() != null && request.version() < 1) {
            throw new PolicyValidationException("Policy version must be positive");
        }
        if (request.effectiveFrom() != null && request.effectiveTo() != null
                && request.effectiveTo().isBefore(request.effectiveFrom())) {
            throw new PolicyValidationException("Policy effective-to date cannot be before effective-from date");
        }
        if (request.rules() != null) {
            for (PolicyRuleRequest rule : request.rules()) {
                if (rule == null || isBlank(rule.ruleCode()) || rule.ruleType() == null
                        || isBlank(rule.ruleValue()) || rule.severity() == null) {
                    throw new PolicyValidationException("Each policy rule requires code, type, value, and severity");
                }
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private TravelPolicyResponse toResponse(TravelPolicy policy) {
        List<PolicyRuleResponse> rules = policy.getRules().stream()
                .map(rule -> new PolicyRuleResponse(
                        rule.getId(),
                        rule.getRuleCode(),
                        rule.getRuleType(),
                        rule.getRuleValue(),
                        rule.getSeverity(),
                        rule.getDescription(),
                        rule.isActive()))
                .toList();
        return new TravelPolicyResponse(
                policy.getId(),
                policy.getName(),
                policy.getDescription(),
                policy.getPolicyType(),
                policy.getVersion(),
                policy.getEffectiveFrom(),
                policy.getEffectiveTo(),
                policy.getGrade(),
                policy.isActive(),
                rules);
    }
}
