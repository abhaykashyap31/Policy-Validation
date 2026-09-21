package com.company.travelplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.travelplanner.dto.BookingPolicyValidationRequest;
import com.company.travelplanner.dto.BookingPolicyValidationResponse;
import com.company.travelplanner.service.PolicyValidationService;

@RestController
@RequestMapping("/api/policy-validations")
public class PolicyValidationController {

    private final PolicyValidationService policyValidationService;

    public PolicyValidationController(PolicyValidationService policyValidationService) {
        this.policyValidationService = policyValidationService;
    }

    @PostMapping("/booking")
    public ResponseEntity<BookingPolicyValidationResponse> validateBooking(
            @RequestBody BookingPolicyValidationRequest request) {
        return ResponseEntity.ok(policyValidationService.validateBooking(request));
    }
}
