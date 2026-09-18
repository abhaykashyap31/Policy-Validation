package com.company.travelplanner.controller;

import com.company.travelplanner.dto.TravelRequestCreateRequest;
import com.company.travelplanner.dto.TravelRequestResponse;
import com.company.travelplanner.service.TravelRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/travel-requests")
public class TravelRequestController {

    private final TravelRequestService travelRequestService;

    public TravelRequestController(TravelRequestService travelRequestService) {
        this.travelRequestService = travelRequestService;
    }

    @PostMapping
    public ResponseEntity<TravelRequestResponse> createRequest(@RequestBody TravelRequestCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(travelRequestService.createTravelRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<TravelRequestResponse>> getAllRequests() {
        return ResponseEntity.ok(travelRequestService.getAllTravelRequests());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelRequestResponse> updateRequest(@PathVariable Long id,
                                                               @RequestBody TravelRequestCreateRequest request) {
        return ResponseEntity.ok(travelRequestService.updateTravelRequest(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        travelRequestService.deleteTravelRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelRequestResponse> getRequest(@PathVariable Long id) {
        return ResponseEntity.ok(travelRequestService.getTravelRequest(id));
    }
}
