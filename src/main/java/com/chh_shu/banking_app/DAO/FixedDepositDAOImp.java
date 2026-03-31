package com.chh_shu.banking_app.DAO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chh_shu.banking_app.Entity.FixedDepositEntity;
import com.chh_shu.banking_app.Entity.FixedDepositRepo;
import com.chh_shu.banking_app.Models.FixedDeposit;

@Repository
public class FixedDepositDAOImp implements FixedDepositDAO {

    @Autowired
    private FixedDepositRepo fdRepo;

    // Helper to convert Entity → Model (DRY principle)
    private FixedDeposit convertToModel(FixedDepositEntity entity) {
        if (entity == null) return null;

        FixedDeposit model = new FixedDeposit();
        model.setFdId(entity.getFdId());
        model.setAccountNo(entity.getAccountNo());
        model.setPrincipalAmount(entity.getPrincipalAmount());
        model.setTenureMonths(entity.getTenureMonths());
        model.setInterestRate(entity.getInterestRate());
        model.setMaturityDate(entity.getMaturityDate());
        model.setMaturityAmount(entity.getMaturityAmount());
        model.setStatus(entity.getStatus() != null ? entity.getStatus().name() : "ACTIVE");
        model.setCreatedAt(entity.getCreatedAt());
        return model;
    }

    @Override
    public String saveFd(Long accountNo, double principalAmount, int tenureMonths, 
                         double interestRate, LocalDate maturityDate, double maturityAmount) {

        FixedDepositEntity fdEntity = new FixedDepositEntity();
        fdEntity.setAccountNo(accountNo);
        fdEntity.setPrincipalAmount(principalAmount);
        fdEntity.setTenureMonths(tenureMonths);
        fdEntity.setInterestRate(interestRate);
        fdEntity.setMaturityDate(maturityDate);
        fdEntity.setMaturityAmount(maturityAmount);
        // status already default ACTIVE hai entity mein

        fdRepo.save(fdEntity);

        return String.format("""
            Fixed Deposit created successfully!
            Account No     : %d
            Principal      : ₹%.2f
            Tenure         : %d months
            Interest Rate  : %.2f%%
            Maturity Amount: ₹%.2f
            Maturity Date  : %s""",
            accountNo, principalAmount, tenureMonths, interestRate, maturityAmount, maturityDate);
    }

    @Override
    public List<FixedDeposit> getFDByAccount(Long accountNo) {
        return fdRepo.findAll().stream()
                .filter(fd -> fd.getAccountNo().equals(accountNo))
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<FixedDeposit> getAllFD() {
        return fdRepo.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public FixedDeposit getFDById(Long fdId) {
        FixedDepositEntity entity = fdRepo.findById(fdId)
                .orElseThrow(() -> new RuntimeException("FD not found with ID: " + fdId));

        return convertToModel(entity);
    }

    @Override
    public boolean updateFD(Long fdId, FixedDeposit fd) {
        return fdRepo.findById(fdId).map(entity -> {
            if (fd.getPrincipalAmount() != null) entity.setPrincipalAmount(fd.getPrincipalAmount());
            if (fd.getTenureMonths() != 0) entity.setTenureMonths(fd.getTenureMonths());
            if (fd.getInterestRate() != null) entity.setInterestRate(fd.getInterestRate());
            if (fd.getMaturityDate() != null) entity.setMaturityDate(fd.getMaturityDate());
            if (fd.getMaturityAmount() != null) entity.setMaturityAmount(fd.getMaturityAmount());
            if (fd.getStatus() != null) {
                try {
                    entity.setStatus(FixedDepositEntity.FDStatus.valueOf(fd.getStatus().toUpperCase()));
                } catch (IllegalArgumentException ignored) {}
            }
            fdRepo.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean closeFD(Long fdId) {
        return fdRepo.findById(fdId).map(fd -> {
            fd.setStatus(FixedDepositEntity.FDStatus.PREMATURELY_CLOSED);
            fdRepo.save(fd);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean checkAndMatureFD(Long fdId) {
        return fdRepo.findById(fdId).map(fd -> {
            if (fd.getStatus() == FixedDepositEntity.FDStatus.ACTIVE) {
                LocalDate today = LocalDate.now();
                boolean dateChanged = false;

                // ✅ Sync maturityDate with createdAt + tenureMonths (source of truth)
                if (fd.getCreatedAt() != null && fd.getTenureMonths() > 0) {
                    LocalDate correctMaturityDate = fd.getCreatedAt()
                                                    .toLocalDate()
                                                    .plusMonths(fd.getTenureMonths());

                    if (!correctMaturityDate.equals(fd.getMaturityDate())) {
                        fd.setMaturityDate(correctMaturityDate); // correct the stale date
                        dateChanged = true;
                    }
                }

                // ✅ Now check if maturity has passed
                if (fd.getMaturityDate() != null && !fd.getMaturityDate().isAfter(today)) {
                    fd.setStatus(FixedDepositEntity.FDStatus.MATURED);
                    fdRepo.save(fd);
                    return true;
                }

                // ✅ Save even if not matured yet — date was corrected
                if (dateChanged) {
                    fdRepo.save(fd);
                }
            }
            return false;
        }).orElse(false);
    }
}