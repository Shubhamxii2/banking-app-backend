package com.chh_shu.banking_app.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chh_shu.banking_app.Entity.AccountEntity;
import com.chh_shu.banking_app.Entity.AccountRepo;
import com.chh_shu.banking_app.Models.Account;

@Repository
public class AccountDaoImp implements AccountDAO {

    @Autowired
    private AccountRepo accountRepo; 
    
     
    @Override
    public String addAccount(Account account) {
        AccountEntity accEntity = new AccountEntity();
        BeanUtils.copyProperties(account, accEntity);
        System.out.println("Saving Account to the DB");
        
        accountRepo.save(accEntity);
        return "Account Saved Sucessfully";
    }
    
    @Override
    public List<Account> getAllAccounts() {
        List<AccountEntity> accountList = accountRepo.findAll();
        List<Account> accounts = new ArrayList<>();
        System.out.println("Fetching Att the Account form DB\n");
        if(!accountList.isEmpty()){
            for(AccountEntity accountEntity: accountList){
                accounts.add(entityToModel(accountEntity));
            }
            return accounts;
            
        }else{ return null;}
    }
    
    @Override
    public Account getAccountById(Long accountNo) {
        System.out.println("Fetching Account details from DB by using Account No");
        Optional<AccountEntity> optionalEntity = accountRepo.findById(accountNo);
        if(optionalEntity.isPresent()){
            AccountEntity accEntity = optionalEntity.get();
            return entityToModel(accEntity);
        }
        return null;
    }

    @Override
    public boolean updateAccount(Long accountNo, Account account) {
        System.out.println("Updating Account Details to DB");
        Optional<AccountEntity> optionalEntity = accountRepo.findById(accountNo);
        if(optionalEntity.isPresent()){
            AccountEntity existingAccount = optionalEntity.get();
            // BeanUtils.copyProperties(account, existingAccount); // This is copying everything including accountNo, which is causing error
            existingAccount.setName(account.getName());
            existingAccount.setBalance(account.getBalance());
            existingAccount.setType(account.getType());
            
            accountRepo.save(existingAccount);
            System.out.println();
            System.out.println("Account updated successfully!");
            System.out.println();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteAccount(Long accountNo) {
        System.out.println("Deleting Account in DB");
        Optional<AccountEntity> optionalEntity = accountRepo.findById(accountNo);
        if(optionalEntity.isPresent()){
            accountRepo.deleteById(accountNo);
            return true;  
        }else return false;
    }
    private Account entityToModel(AccountEntity entity){
        if(entity == null) return null;
        Account account = new Account(); 
        account.setAccountNo(entity.getAccountNo());
        account.setName(entity.getName());
        account.setType(entity.getType());
        account.setBalance(entity.getBalance());
        account.setCreatedDate(entity.getCreatedDate());
        account.setStatus(entity.getAccStatus() != null ? entity.getAccStatus().name() : "WORKING");

        return account;
    }
    
}
