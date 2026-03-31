package com.chh_shu.banking_app.Models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long txnId;

    private Long accountNo;           // jis account pe transaction hui
    private String txnType;           // DEPOSIT, WITHDRAW, TRANSFER_DEBIT, TRANSFER_CREDIT
    private double amount;
    private double balanceAfter;
    private Long oppositeAccount;     // transfer ke case mein doosra account, warna null
    private LocalDateTime timestamp;
    private String description;
    
}
