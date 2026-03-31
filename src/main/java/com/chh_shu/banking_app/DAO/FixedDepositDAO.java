package com.chh_shu.banking_app.DAO;

import java.time.LocalDate;
import java.util.List;

import com.chh_shu.banking_app.Models.FixedDeposit;

public interface FixedDepositDAO {
        // Create
    String saveFd(Long accountNo, double principalAmount, int tenureMonths, double interestRate, LocalDate maturityDate, double maturityAmount);
    // Read 
    List<FixedDeposit> getFDByAccount(Long accountNo);
    List<FixedDeposit> getAllFD();
    FixedDeposit getFDById(Long fdId);
    boolean checkAndMatureFD(Long fdId);

    // Update
    boolean updateFD(Long fdId, FixedDeposit fd);

    // Delete
    boolean closeFD (Long fdId);
}
