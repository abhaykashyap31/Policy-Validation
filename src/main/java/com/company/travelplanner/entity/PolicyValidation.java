package com.company.travelplanner.entity;

import com.company.travelplanner.common.enums.ValidationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policy_validation")
public class PolicyValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private TravelPolicy policy;

    private LocalDateTime validatedAt;

    @Enumerated(EnumType.STRING)
    private ValidationStatus overallStatus;

    @OneToMany(mappedBy = "validation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PolicyViolation> violations = new ArrayList<>();

    public Long getId() { return id; }
    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }
    public TravelPolicy getPolicy() { return policy; }
    public void setPolicy(TravelPolicy policy) { this.policy = policy; }
    public LocalDateTime getValidatedAt() { return validatedAt; }
    public void setValidatedAt(LocalDateTime validatedAt) { this.validatedAt = validatedAt; }
    public ValidationStatus getOverallStatus() { return overallStatus; }
    public void setOverallStatus(ValidationStatus overallStatus) { this.overallStatus = overallStatus; }
    public List<PolicyViolation> getViolations() { return violations; }
}
