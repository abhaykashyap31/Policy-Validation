package com.company.travelplanner.policy.controller;

import com.company.travelplanner.policy.dto.PolicyValidationResponse;
import com.company.travelplanner.policy.dto.PolicyValidationRequest;
import com.company.travelplanner.policy.service.PolicyValidationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/policy-validations")
public class PolicyValidationController {

    private final PolicyValidationService policyValidationService;

    public PolicyValidationController(PolicyValidationService policyValidationService) {
        this.policyValidationService = policyValidationService;
    }

    @PostMapping("/travel-request/{travelRequestId}")
    public ResponseEntity<PolicyValidationResponse> validate(@PathVariable Long travelRequestId) {
        return ResponseEntity.ok(policyValidationService.validate(travelRequestId));
    }

    @PostMapping
    public ResponseEntity<PolicyValidationResponse> validate(
            @RequestBody PolicyValidationRequest request) {
        return ResponseEntity.ok(policyValidationService.validate(request.travelRequestId()));
    }
}
