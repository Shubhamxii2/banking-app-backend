package com.chh_shu.banking_app.Service;

import java.util.List;

import com.chh_shu.banking_app.Models.StatementResponse;
import com.chh_shu.banking_app.Models.Transaction;

public interface TransactionService {
    
    String deposit(Long accountNo, double amount, String description);
    
    String withdraw(Long accountNo, double amount, String description);
    
    String transfer(Long fromAccNo, Long toAccNo, double amount, String description);

    List<Transaction> getTransactionsByAccount(Long accountNo);
    
    Transaction getTransactionById(Long txnId);

    List<Transaction> getMiniStatement(Long accountNo);
    
    List<Transaction> getFullStatement(Long accountNo);
    StatementResponse getFullStatements(Long accountNo);
    byte[] generateFullStatementPdf(Long accountNo);

    
    
}
