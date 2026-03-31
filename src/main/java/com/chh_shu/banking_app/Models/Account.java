package com.chh_shu.banking_app.Models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Long accountNo;
    private String name;
    private String type;
    private double balance;
    
    private LocalDateTime createdDate;

    // 🔥 Naya field
    private String status;   // Default: "WORKING"
    public enum AccountStatus {
        WORKING,
        BLOCKED,
        CLOSED,
        FROZEN
    }
    // private Users userId;
    // private String username;
}