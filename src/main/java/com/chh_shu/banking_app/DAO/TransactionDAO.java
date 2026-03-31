package com.chh_shu.banking_app.DAO;

import java.util.List;

import com.chh_shu.banking_app.Models.Transaction;

public interface TransactionDAO {
    void saveTransaction(Transaction txn);
    List<Transaction> getTransactionsByAccount(Long accountNo);
    Transaction getTransactionById(Long txnId);
    // List<Transaction> getMiniStatement(Long accountNo);

}
