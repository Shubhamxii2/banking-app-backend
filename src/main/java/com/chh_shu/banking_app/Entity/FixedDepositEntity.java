package com.chh_shu.banking_app.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "fixed_deposits")
@Data
public class FixedDepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "fd_id")
    private Long fdId;

    private Long accountNo;

    private double principalAmount; 
    private int tenureMonths;
    private double interestRate;

    private LocalDate maturityDate;
    private double maturityAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FDStatus status = FDStatus.ACTIVE;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; 

    public enum FDStatus{
        ACTIVE, MATURED, PREMATURELY_CLOSED
    }
}
