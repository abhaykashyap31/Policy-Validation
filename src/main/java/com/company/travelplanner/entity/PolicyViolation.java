package com.company.travelplanner.entity;

import com.company.travelplanner.common.enums.Severity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "policy_violation")
public class PolicyViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "validation_id", nullable = false)
    private PolicyValidation validation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_rule_id", nullable = false)
    private PolicyRule policyRule;

    private String message;
    private String violationType;
    private String actualValue;
    private String allowedValue;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private boolean resolved;

    public Long getId() { return id; }
    public PolicyValidation getValidation() { return validation; }
    public void setValidation(PolicyValidation validation) { this.validation = validation; }
    public PolicyRule getPolicyRule() { return policyRule; }
    public void setPolicyRule(PolicyRule policyRule) { this.policyRule = policyRule; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getViolationType() { return violationType; }
    public void setViolationType(String violationType) { this.violationType = violationType; }
    public String getActualValue() { return actualValue; }
    public void setActualValue(String actualValue) { this.actualValue = actualValue; }
    public String getAllowedValue() { return allowedValue; }
    public void setAllowedValue(String allowedValue) { this.allowedValue = allowedValue; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
}
