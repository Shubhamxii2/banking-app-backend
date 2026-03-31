package com.chh_shu.banking_app.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chh_shu.banking_app.DAO.AccountDAO;
import com.chh_shu.banking_app.DAO.FixedDepositDAO;
import com.chh_shu.banking_app.Models.Account;
import com.chh_shu.banking_app.Models.FixedDeposit;

@Service
public class FdServiceImp implements FdService {

    @Autowired
    private AccountDAO accDAO;
    
    @Autowired
    private TransactionService txnService;

    @Autowired
    private FixedDepositDAO fdDAO;

    public static final Long BANK_ACCOUNT_NO = 9999999999L;
    public static final double RATE = 7.0;

    @Override
    @Transactional
    public String createFixedDeposit(FixedDeposit fd) {
        System.out.println(".createFD() is called");

        Long accountNo = fd.getAccountNo();
        Double principalAmount = fd.getPrincipalAmount();
        Integer tenureMonths = fd.getTenureMonths();
        double interestRate = RATE;

        // Validation
        if (principalAmount == null || principalAmount <= 0) 
            return "Amount must be greater than Zero";

        if (tenureMonths == null || tenureMonths < 1) 
            return "Tenure must be at least 1 month";

        Account acc = accDAO.getAccountById(accountNo);
        if (acc == null) return "Account Not Found";
        if (acc.getBalance() < principalAmount) return "Insufficient Balance in Account";

        // Money Transfer to Bank Account for FD
        txnService.transfer(accountNo, BANK_ACCOUNT_NO, principalAmount, "Fixed Deposit Created");

        // Interest Calculation
        double interest = (principalAmount * interestRate * tenureMonths) / 1200;
        double maturityAmount = principalAmount + interest;
        LocalDate maturityDate = LocalDate.now().plusMonths(tenureMonths);

        // Save FD via DAO
        return fdDAO.saveFd(accountNo, principalAmount, tenureMonths, RATE, maturityDate, maturityAmount);
    }

    @Override
    public List<FixedDeposit> getAllFDsByAccount(Long accountNo) {
        List<FixedDeposit> fds = fdDAO.getFDByAccount(accountNo);
        // Auto-mature check for each FD
        fds.forEach(fd -> checkAndMatureIfNeeded(fd.getFdId()));
        return fds;
    }

    @Override
    public FixedDeposit getFDById(Long fdId) {
        checkAndMatureIfNeeded(fdId);
        return fdDAO.getFDById(fdId);
    }

    @Override
    public List<FixedDeposit> getAllFDs() {
        List<FixedDeposit> fds = fdDAO.getAllFD();
        fds.forEach(fd -> checkAndMatureIfNeeded(fd.getFdId()));
        return fds;
    }

    @Override
    public String updateFD(Long fdId, FixedDeposit fd) {
        boolean result = fdDAO.updateFD(fdId, fd);
        return result ? "FD Updated Successfully" : "Something went Wrong";
    }

    // Private helper - Service only calls DAO
    private void checkAndMatureIfNeeded(Long fdId) {
        fdDAO.checkAndMatureFD(fdId);
    }

    @Override
    @Transactional
    public String checkAndMatureFD(Long fdId) {
        boolean matured = fdDAO.checkAndMatureFD(fdId);
        return matured ? "FD status updated to MATURED" : "FD is still ACTIVE or not found";
    }
}