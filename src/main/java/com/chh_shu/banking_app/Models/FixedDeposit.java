package com.chh_shu.banking_app.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedDeposit {
    private Long fdId;
    private Long accountNo;
    private Double principalAmount;
    private Double interestRate;
    private Double maturityAmount;
    private int tenureMonths;
    private LocalDate maturityDate;
    private String status;
    private LocalDateTime createdAt;
}
