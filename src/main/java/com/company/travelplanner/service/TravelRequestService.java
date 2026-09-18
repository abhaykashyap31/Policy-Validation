package com.company.travelplanner.service;

import com.company.travelplanner.common.enums.TravelRequestStatus;
import com.company.travelplanner.common.enums.TravelMode;
import com.company.travelplanner.common.exception.InvalidTravelRequestException;
import com.company.travelplanner.common.exception.ResourceNotFoundException;
import com.company.travelplanner.entity.Employee;
import com.company.travelplanner.entity.Booking;
import com.company.travelplanner.entity.BookingFlag;
import com.company.travelplanner.repository.EmployeeRepository;
import com.company.travelplanner.repository.BookingRepository;
import com.company.travelplanner.entity.TravelRequest;
import com.company.travelplanner.dto.TravelRequestCreateRequest;
import com.company.travelplanner.dto.TravelRequestResponse;
import java.math.BigDecimal;
import com.company.travelplanner.repository.TravelRequestRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TravelRequestService {

    private final TravelRequestRepository travelRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;

    public TravelRequestService(TravelRequestRepository travelRequestRepository,
                                EmployeeRepository employeeRepository,
                                BookingRepository bookingRepository) {
        this.travelRequestRepository = travelRequestRepository;
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
    }

    public TravelRequestResponse createTravelRequest(TravelRequestCreateRequest request) {
        validate(request);
        Employee employee = employeeRepository.findFirstByGradeAndActiveTrue(request.employeeGrade())
                .orElseThrow(() -> new ResourceNotFoundException("Active employee not found for grade: " + request.employeeGrade()));
        TravelRequest travelRequest = new TravelRequest();
        travelRequest.setEmployee(employee);
        travelRequest.setSourceCity(request.sourceCity());
        travelRequest.setDestinationCity(request.destinationCity());
        travelRequest.setTravelMode(request.travelMode());
        travelRequest.setDistance(request.distance());
        travelRequest.setExpense(request.expense());
        travelRequest.setEstimatedCost(expectedCost(request.travelMode(), request.distance()));
        travelRequest.setStatus(TravelRequestStatus.DRAFT);
        LocalDateTime now = LocalDateTime.now();
        travelRequest.setCreatedAt(now);
        travelRequest.setUpdatedAt(now);
        TravelRequest savedRequest = travelRequestRepository.save(travelRequest);
        Booking booking = new Booking();
        booking.setTravelRequest(savedRequest);
        booking.setExpectedCost(savedRequest.getEstimatedCost());
        booking.setExpense(savedRequest.getExpense());
        booking.setFlag(calculateFlag(savedRequest.getTravelMode(), savedRequest.getEstimatedCost(), savedRequest.getExpense()));
        booking.setValid(true);
        booking.setBookedAt(now);
        return toResponse(savedRequest, bookingRepository.save(booking));
    }

    public TravelRequestResponse getTravelRequest(Long id) {
        return travelRequestRepository.findById(id)
            .map(request -> bookingRepository.findByTravelRequestId(request.getId())
                .map(booking -> toResponse(request, booking))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for travel request: " + id)))
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found: " + id));
    }

        @Transactional(readOnly = true)
        public java.util.List<TravelRequestResponse> getAllTravelRequests() {
        return travelRequestRepository.findAll().stream()
            .map(request -> bookingRepository.findByTravelRequestId(request.getId())
                .map(booking -> toResponse(request, booking))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for travel request: " + request.getId())))
            .toList();
        }

        @Transactional
        public TravelRequestResponse updateTravelRequest(Long id, TravelRequestCreateRequest request) {
        validate(request);
        TravelRequest travelRequest = travelRequestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Travel request not found: " + id));
        Employee employee = employeeRepository.findFirstByGradeAndActiveTrue(request.employeeGrade())
            .orElseThrow(() -> new ResourceNotFoundException("Active employee not found for grade: " + request.employeeGrade()));
        travelRequest.setEmployee(employee);
        travelRequest.setSourceCity(request.sourceCity());
        travelRequest.setDestinationCity(request.destinationCity());
        travelRequest.setTravelMode(request.travelMode());
        travelRequest.setDistance(request.distance());
        travelRequest.setExpense(request.expense());
        travelRequest.setEstimatedCost(expectedCost(request.travelMode(), request.distance()));
        travelRequest.setUpdatedAt(LocalDateTime.now());
        TravelRequest savedRequest = travelRequestRepository.save(travelRequest);
        Booking booking = bookingRepository.findByTravelRequestId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found for travel request: " + id));
        booking.setExpectedCost(savedRequest.getEstimatedCost());
        booking.setExpense(savedRequest.getExpense());
        booking.setFlag(calculateFlag(savedRequest.getTravelMode(), savedRequest.getEstimatedCost(), savedRequest.getExpense()));
        booking.setValid(true);
        return toResponse(savedRequest, bookingRepository.save(booking));
        }

        @Transactional
        public void deleteTravelRequest(Long id) {
        TravelRequest travelRequest = travelRequestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Travel request not found: " + id));
        bookingRepository.findByTravelRequestId(id).ifPresent(bookingRepository::delete);
        travelRequestRepository.delete(travelRequest);
        }

    private void validate(TravelRequestCreateRequest request) {
        if (isBlank(request.sourceCity()) || isBlank(request.destinationCity())
                || isBlank(request.employeeGrade()) || request.travelMode() == null
                || request.distance() == null || request.expense() == null) {
            throw new InvalidTravelRequestException("Source, destination, distance, mode, employee grade, and expense are required");
        }
        if (request.distance().compareTo(BigDecimal.ZERO) <= 0 || request.expense().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTravelRequestException("Distance must be positive and expense cannot be negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private BigDecimal expectedCost(TravelMode travelMode, BigDecimal distance) {
        return switch (travelMode) {
            case CAB -> distance.multiply(BigDecimal.valueOf(25));
            case TRAIN -> distance.multiply(BigDecimal.TEN);
            case FLIGHT -> null;
        };
    }

    private TravelRequestResponse toResponse(TravelRequest request, Booking booking) {
        return new TravelRequestResponse(
            request.getId(),
                request.getSourceCity(),
                request.getDestinationCity(),
                request.getDistance(),
                request.getTravelMode(),
                request.getEmployee().getGrade(),
                request.getExpense(),
                booking.getId(),
                booking.getExpectedCost(),
                booking.getFlag().name().charAt(0),
                booking.isValid());
    }

    private BookingFlag calculateFlag(TravelMode mode, BigDecimal expectedCost, BigDecimal expense) {
        if (mode == TravelMode.FLIGHT || expectedCost == null || expense.compareTo(expectedCost) <= 0) {
            return BookingFlag.G;
        }
        BigDecimal overagePercent = expense.subtract(expectedCost)
                .multiply(BigDecimal.valueOf(100))
                .divide(expectedCost, 4, java.math.RoundingMode.HALF_UP);
        if (overagePercent.compareTo(BigDecimal.valueOf(30)) <= 0) {
            return BookingFlag.G;
        }
        return overagePercent.compareTo(BigDecimal.valueOf(75)) <= 0 ? BookingFlag.Y : BookingFlag.R;
    }
}
