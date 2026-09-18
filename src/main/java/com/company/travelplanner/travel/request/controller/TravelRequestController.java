package com.company.travelplanner.travel.request.controller;

import com.company.travelplanner.travel.request.dto.TravelRequestCreateRequest;
import com.company.travelplanner.travel.request.dto.TravelRequestResponse;
import com.company.travelplanner.travel.request.service.TravelRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/travel-requests")
public class TravelRequestController {

    private final TravelRequestService travelRequestService;

    public TravelRequestController(TravelRequestService travelRequestService) {
        this.travelRequestService = travelRequestService;
    }

    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<TravelRequestResponse> createRequest(@PathVariable Long employeeId,
                                                               @RequestBody TravelRequestCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(travelRequestService.createTravelRequest(employeeId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelRequestResponse> getRequest(@PathVariable Long id) {
        return ResponseEntity.ok(travelRequestService.getTravelRequest(id));
    }
}
