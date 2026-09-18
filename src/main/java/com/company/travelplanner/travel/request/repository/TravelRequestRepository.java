package com.company.travelplanner.travel.request.repository;

import com.company.travelplanner.common.enums.TravelRequestStatus;
import com.company.travelplanner.travel.request.entity.TravelRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {
    List<TravelRequest> findByEmployeeId(Long employeeId);
    List<TravelRequest> findByStatus(TravelRequestStatus status);
}
