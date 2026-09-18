package com.company.travelplanner.travel.request.service;

import com.company.travelplanner.common.enums.TravelRequestStatus;
import com.company.travelplanner.common.enums.TravelMode;
import com.company.travelplanner.common.exception.InvalidTravelRequestException;
import com.company.travelplanner.common.exception.ResourceNotFoundException;
import com.company.travelplanner.entity.Employee;
import com.company.travelplanner.repository.EmployeeRepository;
import com.company.travelplanner.travel.request.entity.TravelRequest;
import com.company.travelplanner.travel.request.dto.TravelRequestCreateRequest;
import com.company.travelplanner.travel.request.dto.TravelRequestResponse;
import java.math.BigDecimal;
import com.company.travelplanner.travel.request.repository.TravelRequestRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class TravelRequestService {

    private final TravelRequestRepository travelRequestRepository;
    private final EmployeeRepository employeeRepository;

    public TravelRequestService(TravelRequestRepository travelRequestRepository,
                                EmployeeRepository employeeRepository) {
        this.travelRequestRepository = travelRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    public TravelRequestResponse createTravelRequest(Long employeeId, TravelRequestCreateRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));
        validate(request);
        TravelRequest travelRequest = new TravelRequest();
        travelRequest.setEmployee(employee);
        travelRequest.setPurpose(request.purpose());
        travelRequest.setSourceCity(request.sourceCity());
        travelRequest.setDestinationCity(request.destinationCity());
        travelRequest.setDepartureDate(request.departureDate());
        travelRequest.setReturnDate(request.returnDate());
        travelRequest.setTravelMode(request.travelMode());
        travelRequest.setTravelClass(request.travelClass());
        travelRequest.setEstimatedCost(request.estimatedCost());
        travelRequest.setStatus(TravelRequestStatus.DRAFT);
        LocalDateTime now = LocalDateTime.now();
        travelRequest.setCreatedAt(now);
        travelRequest.setUpdatedAt(now);
        return toResponse(travelRequestRepository.save(travelRequest));
    }

    public TravelRequestResponse getTravelRequest(Long id) {
        return travelRequestRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found: " + id));
    }

    private void validate(TravelRequestCreateRequest request) {
        if (isBlank(request.purpose()) || isBlank(request.sourceCity()) || isBlank(request.destinationCity())
                || request.departureDate() == null || request.returnDate() == null || request.travelMode() == null) {
            throw new InvalidTravelRequestException("Purpose, cities, dates, and travel mode are required");
        }
        if (request.travelMode() == TravelMode.FLIGHT && request.travelClass() == null) {
            throw new InvalidTravelRequestException("Travel class is required for flight travel");
        }
        if (request.returnDate().isBefore(request.departureDate())) {
            throw new InvalidTravelRequestException("Return date cannot be before departure date");
        }
        if (request.estimatedCost() != null && request.estimatedCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTravelRequestException("Estimated cost cannot be negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private TravelRequestResponse toResponse(TravelRequest request) {
        return new TravelRequestResponse(
                request.getId(),
                request.getEmployee().getId(),
                request.getPurpose(),
                request.getSourceCity(),
                request.getDestinationCity(),
                request.getDepartureDate(),
                request.getReturnDate(),
                request.getTravelMode(),
                request.getTravelClass(),
                request.getEstimatedCost(),
                request.getStatus());
    }
}
