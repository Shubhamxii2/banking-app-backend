package com.chh_shu.banking_app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementResponse {
    private Long accountNo;
    private String accountHolderName;
    private String accountType;
    private double currentBalance;

    private double totalCredit;
    private double totalDebit;

    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private List<Transaction> txns;
}
