package com.chh_shu.banking_app.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chh_shu.banking_app.Entity.TransactionEntity;
import com.chh_shu.banking_app.Entity.TransactionRepo;
import com.chh_shu.banking_app.Models.Transaction;

@Repository
public class TransactionDaoImp implements TransactionDAO{

    @Autowired
    private TransactionRepo txnRepo;
    
    @Override
    public void saveTransaction(Transaction txn) {
        TransactionEntity txnEntity =  new TransactionEntity();
        BeanUtils.copyProperties(txn, txnEntity);
        System.out.println("Saving Transaction to DB");
        txnRepo.save(txnEntity);
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Long accountNo) {
        List<Transaction> txns = new ArrayList<>();
        List<TransactionEntity> txnsList = txnRepo.findAll();
        System.out.println("Fetching all the Transactions of an Account from DB");
        for(TransactionEntity txnEntity: txnsList){
            if(!txnsList.isEmpty() && accountNo.equals(txnEntity.getAccountNo())){
                Transaction txn = new Transaction();
                BeanUtils.copyProperties(txnEntity, txn);
                txns.add(txn);
            }
        }
        return txns;
    }

    @Override
    public Transaction getTransactionById(Long txnId) {
        Transaction txn = new Transaction();
        Optional<TransactionEntity> optTxnEntity = txnRepo.findById(txnId);
        System.out.println("Fetching Transaction By ID from DB");
        if(!optTxnEntity.isEmpty()){
            TransactionEntity txnEntity = optTxnEntity.get();
            BeanUtils.copyProperties(txnEntity, txn);
            return txn;
        }else{
            return null;
        }
    }
    
}
