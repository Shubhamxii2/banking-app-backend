package com.chh_shu.banking_app.DAO;

import java.util.List;

import com.chh_shu.banking_app.Models.Account;

// @Repository
public interface AccountDAO {
    // Create
    String addAccount(Account account);

    // Read 
    List<Account> getAllAccounts();
    Account getAccountById(Long accountNo);

    // Update
    boolean updateAccount(Long accountNo, Account account);

    // Delete
    boolean deleteAccount(Long accountNo);
}
