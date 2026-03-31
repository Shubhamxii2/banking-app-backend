package com.chh_shu.banking_app.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chh_shu.banking_app.DAO.AccountDAO;
import com.chh_shu.banking_app.DAO.TransactionDAO;
import com.chh_shu.banking_app.Models.Account;
import com.chh_shu.banking_app.Models.StatementResponse;
import com.chh_shu.banking_app.Models.Transaction;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;                    // ← ByteArrayOutputStream
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class TransactionServiceImp implements TransactionService {

    @Autowired
    private AccountDAO accDAO;

    @Autowired   // ← yeh missing tha baby!
    private TransactionDAO txnDAO;

    // ==================== DEPOSIT ====================
    @Override
    @Transactional
    public String deposit(Long accountNo, double amount, String description) {
        if (amount <= 0) return "Amount must be greater than Zero";

        Account acc = accDAO.getAccountById(accountNo);
        if (acc == null) return "Account not found";

        double newBalance = acc.getBalance() + amount;
        acc.setBalance(newBalance);
        accDAO.updateAccount(accountNo, acc);

        saveTransaction(accountNo, "DEPOSIT", amount, newBalance, null, description);

        return "₹" + amount + " deposited successfully.\nNew Balance: ₹" + newBalance;
    }

    // ==================== WITHDRAW ====================
    @Override
    @Transactional
    public String withdraw(Long accountNo, double amount, String description) {
        if (amount <= 0) return "Amount must be greater than Zero";

        Account acc = accDAO.getAccountById(accountNo);
        if (acc == null) return "Account not found";
        if (acc.getBalance() < amount) return "Insufficient Balance";

        double newBalance = acc.getBalance() - amount;
        acc.setBalance(newBalance);
        accDAO.updateAccount(accountNo, acc);

        saveTransaction(accountNo, "WITHDRAW", amount, newBalance, null, description);

        return "₹" + amount + " withdrawn successfully.\nNew Balance: ₹" + newBalance;
    }

    // ==================== TRANSFER ====================
    @Override
    @Transactional
    public String transfer(Long fromAccNo, Long toAccNo, double amount, String description) {
        if (amount <= 0) return "Amount must be greater than Zero";
        if (fromAccNo.equals(toAccNo)) return "Cannot transfer to same account";

        Account fromAcc = accDAO.getAccountById(fromAccNo);
        Account toAcc = accDAO.getAccountById(toAccNo);

        if (fromAcc == null) return "Sender account not found";
        if (toAcc == null) return "Receiver account not found";
        if (fromAcc.getBalance() < amount) return "Insufficient Balance in Sender's Account";

        // Debit from sender
        double fromNewBalance = fromAcc.getBalance() - amount;
        fromAcc.setBalance(fromNewBalance);
        accDAO.updateAccount(fromAccNo, fromAcc);

        // Credit to receiver
        double toNewBalance = toAcc.getBalance() + amount;
        toAcc.setBalance(toNewBalance);
        accDAO.updateAccount(toAccNo, toAcc);

        // Save two transactions
        saveTransaction(fromAccNo, "TRANSFER_DEBIT", amount, fromNewBalance, toAccNo, description);
        saveTransaction(toAccNo, "TRANSFER_CREDIT", amount, toNewBalance, fromAccNo, description);

        return "Transfer successful! ₹" + amount + " from " + fromAccNo + " to " + toAccNo;
    }

    // Private helper to reduce redundancy
    private void saveTransaction(Long accountNo, String type, double amount, double balanceAfter, Long oppositeAcc, String description) {
        Transaction txn = new Transaction();
        txn.setAccountNo(accountNo);
        txn.setTxnType(type);
        txn.setAmount(amount);
        txn.setBalanceAfter(balanceAfter);
        txn.setOppositeAccount(oppositeAcc);
        txn.setDescription(description == null || description.isBlank() 
                        ? "No Description Provided" : description);
        txn.setTimestamp(LocalDateTime.now());

        txnDAO.saveTransaction(txn);
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Long accountNo) {
        return txnDAO.getTransactionsByAccount(accountNo);
    }

    @Override
    public Transaction getTransactionById(Long txnId) {
        return txnDAO.getTransactionById(txnId);
    }

    @Override
    public List<Transaction> getMiniStatement(Long accountNo){
        List<Transaction> allTxns = txnDAO.getTransactionsByAccount(accountNo);
    
    // Sabse recent pehle (timestamp descending)
    allTxns.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));
    
    // Sirf last 5
    if (allTxns.size() > 5) {
        return allTxns.subList(0, 5);
    }
    
    return allTxns;
    }

    @Override
    public StatementResponse getFullStatements(Long accountNo) {
       Account acc = accDAO.getAccountById(accountNo);
        if(acc == null) throw new RuntimeException("Account not Found");

        List<Transaction> txns = txnDAO.getTransactionsByAccount(accountNo);
        txns.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));
        double totalCredit = 0;
        double totalDebit = 0;

        for(Transaction txn: txns){
            String type = txn.getTxnType().toUpperCase();
            if(type.contains("DEPOSIT") || type.contains("CREDIT") || type.contains("TRANSFER_CREDIT")){
                totalCredit += txn.getAmount();
            }else{
                totalDebit += txn.getAmount();
            }
        }
        return new StatementResponse(
            acc.getAccountNo(),
            acc.getName(),
            acc.getType(),
            acc.getBalance(),
            totalCredit,
            totalDebit, 
            txns.isEmpty() ? null : txns.get(txns.size()-1).getTimestamp(),
            LocalDateTime.now(),
            txns
        );
    }

    @Override
    public List<Transaction> getFullStatement(Long accountNo) {
        List<Transaction> transactions = txnDAO.getTransactionsByAccount(accountNo);
    
        // Sort by timestamp - newest first
        transactions.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));
        
        return transactions;
    }

    @Override
    public byte[] generateFullStatementPdf(Long accountNo) {
        StatementResponse statement = getFullStatements(accountNo);  // reuse kiya

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
        ) {

            // Header
            document.add(new Paragraph("BANKING APP - FULL STATEMENT")
                    .setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Generated on: " + LocalDateTime.now())
                    .setFontSize(10).setTextAlignment(TextAlignment.RIGHT));

            // Account Details
            document.add(new Paragraph("\nAccount Details").setBold());
            document.add(new Paragraph("Account No     : " + statement.getAccountNo()));
            document.add(new Paragraph("Holder Name    : " + statement.getAccountHolderName()));
            document.add(new Paragraph("Account Type   : " + statement.getAccountType()));
            document.add(new Paragraph("Current Balance: ₹" + String.format("%.2f", statement.getCurrentBalance())));

            // Summary
            document.add(new Paragraph("\nSummary").setBold());
            document.add(new Paragraph("Total Credit   : ₹" + String.format("%.2f", statement.getTotalCredit())));
            document.add(new Paragraph("Total Debit    : ₹" + String.format("%.2f", statement.getTotalDebit())));
            document.add(new Paragraph("Net Balance    : ₹" + String.format("%.2f", statement.getCurrentBalance())));

            // Transactions Table
            document.add(new Paragraph("\nTransaction History").setBold());

            Table table = new Table(UnitValue.createPercentArray(new float[]{15, 20, 20, 15, 15, 15}))
                    .useAllAvailableWidth();

            table.addHeaderCell("Date");
            table.addHeaderCell("Type");
            table.addHeaderCell("Description");
            table.addHeaderCell("Amount");
            table.addHeaderCell("Balance After");
            table.addHeaderCell("Opposite A/c");

            for (Transaction t : statement.getTxns()) {
                table.addCell(t.getTimestamp().toLocalDate().toString());
                table.addCell(t.getTxnType());
                table.addCell(t.getDescription() != null ? t.getDescription() : "-");
                table.addCell("₹" + String.format("%.2f", t.getAmount()));
                table.addCell("₹" + String.format("%.2f", t.getBalanceAfter()));
                table.addCell(t.getOppositeAccount() != null ? t.getOppositeAccount().toString() : "-");
            }

            document.add(table);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

}