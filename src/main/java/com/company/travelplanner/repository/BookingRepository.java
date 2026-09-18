package com.company.travelplanner.repository;

import com.company.travelplanner.entity.Booking;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByTravelRequestId(Long travelRequestId);
}