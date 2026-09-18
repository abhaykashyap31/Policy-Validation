package com.company.travelplanner.controller;

import com.company.travelplanner.dto.TravelPolicyRequest;
import com.company.travelplanner.dto.TravelPolicyResponse;
import com.company.travelplanner.service.TravelPolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/policies")
public class TravelPolicyController {

    private final TravelPolicyService travelPolicyService;

    public TravelPolicyController(TravelPolicyService travelPolicyService) {
        this.travelPolicyService = travelPolicyService;
    }

    @PostMapping
    public ResponseEntity<TravelPolicyResponse> createPolicy(@RequestBody TravelPolicyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(travelPolicyService.createPolicy(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelPolicyResponse> getPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(travelPolicyService.getPolicy(id));
    }
}
