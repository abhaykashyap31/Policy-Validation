package com.company.travelplanner.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.travelplanner.common.enums.TravelMode;
import com.company.travelplanner.common.enums.TravelRequestStatus;
import com.company.travelplanner.common.enums.ValidationStatus;
import com.company.travelplanner.common.exception.InvalidTravelRequestException;
import com.company.travelplanner.common.exception.PolicyValidationException;
import com.company.travelplanner.common.exception.ResourceNotFoundException;
import com.company.travelplanner.dto.BookingPolicyValidationRequest;
import com.company.travelplanner.dto.BookingPolicyValidationResponse;
import com.company.travelplanner.dto.BookingPolicyValidationResponse.PolicyValidationSummary;
import com.company.travelplanner.dto.PolicyValidationResponse;
import com.company.travelplanner.dto.PolicyViolationResponse;
import com.company.travelplanner.entity.Booking;
import com.company.travelplanner.entity.BookingFlag;
import com.company.travelplanner.entity.Employee;
import com.company.travelplanner.entity.PolicyRule;
import com.company.travelplanner.entity.PolicyValidation;
import com.company.travelplanner.entity.PolicyViolation;
import com.company.travelplanner.entity.TravelPolicy;
import com.company.travelplanner.entity.TravelRequest;
import com.company.travelplanner.repository.BookingRepository;
import com.company.travelplanner.repository.EmployeeRepository;
import com.company.travelplanner.repository.PolicyValidationRepository;
import com.company.travelplanner.repository.TravelPolicyRepository;
import com.company.travelplanner.repository.TravelRequestRepository;
import com.company.travelplanner.validator.PolicyRuleValidator;

@Service
public class PolicyValidationService {

    private final TravelRequestRepository travelRequestRepository;
    private final TravelPolicyRepository travelPolicyRepository;
    private final PolicyValidationRepository policyValidationRepository;
    private final BookingRepository bookingRepository;
    private final EmployeeRepository employeeRepository;
    private final List<PolicyRuleValidator> validators;

    public PolicyValidationService(TravelRequestRepository travelRequestRepository,
                                   TravelPolicyRepository travelPolicyRepository,
                                   PolicyValidationRepository policyValidationRepository,
                                   BookingRepository bookingRepository,
                                   EmployeeRepository employeeRepository,
                                   List<PolicyRuleValidator> validators) {
        this.travelRequestRepository = travelRequestRepository;
        this.travelPolicyRepository = travelPolicyRepository;
        this.policyValidationRepository = policyValidationRepository;
        this.bookingRepository = bookingRepository;
        this.employeeRepository = employeeRepository;
        this.validators = validators;
    }

    @Transactional
    public BookingPolicyValidationResponse validateBooking(BookingPolicyValidationRequest request) {
        validateBookingRequest(request);
        Employee employee = employeeRepository.findFirstByGradeAndActiveTrue(request.employeeGrade())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Active employee not found for grade: " + request.employeeGrade()));

        TravelRequest travelRequest = new TravelRequest();
        travelRequest.setEmployee(employee);
        travelRequest.setSourceCity(request.source());
        travelRequest.setDestinationCity(request.destination());
        travelRequest.setDistance(request.distance());
        travelRequest.setTravelMode(request.mode());
        travelRequest.setExpense(request.expense());
        travelRequest.setDepartureDate(request.date());
        travelRequest.setEstimatedCost(expectedCost(request.mode(), request.distance()));
        travelRequest.setStatus(TravelRequestStatus.DRAFT);
        LocalDateTime now = LocalDateTime.now();
        travelRequest.setCreatedAt(now);
        travelRequest.setUpdatedAt(now);

        TravelRequest savedRequest = travelRequestRepository.save(travelRequest);
        PolicyValidationResponse validation = validate(savedRequest.getId());
        return new BookingPolicyValidationResponse(
            new PolicyValidationSummary(
                validation.overallStatus(),
                validation.bookingValid(),
                validation.bookingFlag()),
                String.valueOf(validation.bookingId()),
            validation.bookingFlag(),
            validation.bookingValid());
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
        Booking booking = bookingRepository.findByTravelRequestId(request.getId()).orElseGet(Booking::new);
        booking.setTravelRequest(request);
        booking.setValid(validation.getViolations().isEmpty());
        booking.setExpectedCost(request.getEstimatedCost());
        booking.setExpense(request.getExpense());
        booking.setFlag(calculateFlag(request.getTravelMode(), request.getEstimatedCost(), request.getExpense()));
        booking.setBookedAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        return toResponse(saved, savedBooking);
    }

    private PolicyValidationResponse toResponse(PolicyValidation validation, Booking booking) {
        List<PolicyViolationResponse> violations = validation.getViolations().stream()
                .map(this::toResponse)
                .toList();
        return new PolicyValidationResponse(
                validation.getId(),
                validation.getTravelRequest().getId(),
                validation.getPolicy().getId(),
                validation.getOverallStatus(),
                validation.getValidatedAt(),
                violations,
                booking.getId(),
                booking.isValid(),
                booking.getFlag().name());
    }

    private void validateBookingRequest(BookingPolicyValidationRequest request) {
        if (request == null || isBlank(request.source()) || isBlank(request.destination())
                || isBlank(request.employeeGrade()) || request.distance() == null
                || request.mode() == null || request.expense() == null || request.date() == null) {
            throw new InvalidTravelRequestException(
                    "Source, destination, distance, mode, employee grade, expense, and date are required");
        }
        if (request.distance().compareTo(BigDecimal.ZERO) <= 0
                || request.expense().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTravelRequestException("Distance must be positive and expense cannot be negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private BigDecimal expectedCost(TravelMode mode, BigDecimal distance) {
        return switch (mode) {
            case CAB -> distance.multiply(BigDecimal.valueOf(25));
            case TRAIN -> distance.multiply(BigDecimal.TEN);
            case FLIGHT -> null;
        };
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

    private PolicyViolationResponse toResponse(PolicyViolation violation) {
        return new PolicyViolationResponse(
                violation.getPolicyRule().getRuleCode(),
                violation.getMessage(),
                violation.getActualValue(),
                violation.getAllowedValue(),
                violation.getSeverity());
    }
}
