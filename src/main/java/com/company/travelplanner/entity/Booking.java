package com.company.travelplanner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false, unique = true)
    private TravelRequest travelRequest;

    private boolean valid;
    private BigDecimal expectedCost;
    private BigDecimal expense;

    @Enumerated(EnumType.STRING)
    private BookingFlag flag;

    private LocalDateTime bookedAt;

    public Long getId() { return id; }
    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public BigDecimal getExpectedCost() { return expectedCost; }
    public void setExpectedCost(BigDecimal expectedCost) { this.expectedCost = expectedCost; }
    public BigDecimal getExpense() { return expense; }
    public void setExpense(BigDecimal expense) { this.expense = expense; }
    public BookingFlag getFlag() { return flag; }
    public void setFlag(BookingFlag flag) { this.flag = flag; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
}