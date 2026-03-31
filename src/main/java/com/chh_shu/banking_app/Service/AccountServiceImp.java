package com.chh_shu.banking_app.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chh_shu.banking_app.DAO.AccountDAO;
import com.chh_shu.banking_app.Models.Account;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountDAO accDAO;
    
    @Override
    public String createAccount(Account account) {
        System.out.println("Creating Account");
        return accDAO.addAccount(account);
    }

    @Override
    public Account getAccountById(Long accountNo) {
        System.out.println("getAccountById() is called");
        return accDAO.getAccountById(accountNo);
    }

    @Override
    public List<Account> getAllAccounts() {
        System.out.println("getAllAccounts() is called");
        return accDAO.getAllAccounts();
    }

    @Override
    public boolean updateAccount(Long accountNo, Account account) {
        System.out.println("UpdateAccount() is called");
        Account acc = accDAO.getAccountById(accountNo);
        if(acc == null) return false;
        
        return accDAO.updateAccount(acc.getAccountNo(), account);
    }

    @Override
    public boolean deleteAccount(Long accountNo) {
        System.out.println("deleteAccount() is called");
        return accDAO.deleteAccount(accountNo);
    }

    @Override
    public String checkBalance(Long accountNo) {
        System.out.println("checkBalance() is called");
        Account acc = accDAO.getAccountById(accountNo);
        if(acc == null) return "Account not Found";

        return "Current Balance\nRs " + acc.getBalance();
    }
    
}
