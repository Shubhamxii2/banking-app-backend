package com.chh_shu.banking_app.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txn_id")
    private Long txnId;

    private Long accountNo;           // jis account pe transaction hui
    private String txnType;           // DEPOSIT, WITHDRAW, TRANSFER_DEBIT, TRANSFER_CREDIT
    private double amount;
    private double balanceAfter;
    private Long oppositeAccount;     // transfer ke case mein doosra account, warna null
    private LocalDateTime timestamp;
    private String description;
     
    // public enum AccountType{
    //     Saving, Current , FD, Loan
    // };
}

