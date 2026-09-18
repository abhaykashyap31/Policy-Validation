package com.company.travelplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.company.travelplanner.dto.TravelPolicyRequest;
import com.company.travelplanner.dto.TravelPolicyResponse;
import com.company.travelplanner.service.TravelPolicyService;

@RestController
@RequestMapping("/api/policies")
public class TravelPolicyController {

    private final TravelPolicyService travelPolicyService;

    public TravelPolicyController(TravelPolicyService travelPolicyService) {
        this.travelPolicyService = travelPolicyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TravelPolicyResponse> createPolicy(@RequestBody TravelPolicyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(travelPolicyService.createPolicy(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<TravelPolicyResponse> getPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(travelPolicyService.getPolicy(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<TravelPolicyResponse>> getAllPolicies() {
        return ResponseEntity.ok(travelPolicyService.getAllPolicies());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TravelPolicyResponse> updatePolicy(@PathVariable Long id,
                                                               @RequestBody TravelPolicyRequest request) {
        return ResponseEntity.ok(travelPolicyService.updatePolicy(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        travelPolicyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}
