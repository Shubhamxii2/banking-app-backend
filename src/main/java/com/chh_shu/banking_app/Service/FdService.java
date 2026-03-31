package com.chh_shu.banking_app.Service;

import java.util.List;

import com.chh_shu.banking_app.Models.FixedDeposit;

public interface FdService {
    // Fixed Deposit
    String createFixedDeposit(FixedDeposit fd);
    List<FixedDeposit> getAllFDsByAccount(Long accountNo);
    FixedDeposit getFDById(Long fdId);
    List<FixedDeposit> getAllFDs();
    String checkAndMatureFD(Long fdId);
    String updateFD(Long fdId, FixedDeposit fd);
}
