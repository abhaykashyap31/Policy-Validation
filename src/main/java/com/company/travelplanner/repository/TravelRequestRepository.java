package com.company.travelplanner.repository;

import com.company.travelplanner.common.enums.TravelRequestStatus;
import com.company.travelplanner.entity.TravelRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {
    List<TravelRequest> findByEmployeeId(Long employeeId);
    List<TravelRequest> findByStatus(TravelRequestStatus status);
}
