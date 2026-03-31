package com.chh_shu.banking_app.Service;

import java.util.List;

import com.chh_shu.banking_app.Models.Account;

public interface AccountService {
    String createAccount(Account account);
    Account getAccountById(Long accountNo);
    List<Account> getAllAccounts();
    boolean updateAccount(Long accountNo, Account account);
    boolean deleteAccount(Long accountNo);
    String checkBalance(Long accountNo);
}
