package com.chh_shu.banking_app.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chh_shu.banking_app.DAO.FixedDepositDAO;
import com.chh_shu.banking_app.Models.Account;
import com.chh_shu.banking_app.Models.FixedDeposit;
import com.chh_shu.banking_app.Models.Transaction;
import com.chh_shu.banking_app.Service.AccountService;
import com.chh_shu.banking_app.Service.FdService;
import com.chh_shu.banking_app.Service.TransactionService;


@RestController
@CrossOrigin("http://localhost:3000/")
public class MyController {
    
    @Autowired
    AccountService accService;
    
    @Autowired
    TransactionService txnService;
    
    @Autowired
    FdService fdService;

    @Autowired
    FixedDepositDAO fdDAO;

    @RequestMapping("hello")
    public String welcome(@RequestParam String name) {
        return "Hello "+ name;
    }
    
    // Create
    @PostMapping("account")
    public String createNewAccount(@RequestBody Account account) {
        accService.createAccount(account);
        return "Account Created Sucessfully";
    }
    
    // Read - Get single account by account number
    @GetMapping("/account/{accountNo}")
    public Account getAccount(@PathVariable Long accountNo) {
        return accService.getAccountById(accountNo);
    }

    @GetMapping("/account/balance-check")
    public String checkBalance(@RequestParam Long accountNo) {
        return accService.checkBalance(accountNo);
    }
    
    @GetMapping("/accounts")
    public List<Account> getAllAccounts(){
        return accService.getAllAccounts();
    }

    @GetMapping("/transaction/by-account")
    public List<Transaction> getTransactionByAccount(@RequestParam Long accountNo) {
        return txnService.getTransactionsByAccount(accountNo);
    }

    @GetMapping("/transaction/{txnId}")
    public Transaction getTransactionById(@PathVariable Long txnId){
        return txnService.getTransactionById(txnId);
        
    }

    // Mini Statement - Last 5 transactions
    @GetMapping("/account/{accNo}/mini-statement")
    public List<Transaction> getMiniStatement(@PathVariable Long accNo) {
        return txnService.getMiniStatement(accNo);
    }
    // Full Statement - Last 5 transactions
    @GetMapping("/account/{accNo}/full-statement")
    public List<Transaction> getFullStatement(@PathVariable Long accNo) {
        return txnService.getFullStatement(accNo);
    }
    
    @GetMapping("/account/{accountNo}/statement/pdf")
    public ResponseEntity<byte[]> downloadStatementPDF(@PathVariable Long accountNo) {
        byte[] pdfBytes = txnService.generateFullStatementPdf(accountNo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=statement_" + accountNo + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // Update
    @PutMapping("account/{accountNo}")
    public String updateExistingAccount(@PathVariable Long accountNo, @RequestBody Account account){
        if( accService.updateAccount(accountNo, account)){
            return "Account Updated Sucessfully";
        }else return "Something went Wrong";
    }

    @PostMapping("/deposit")
    public String depositMoney(@RequestParam Long accountNo, @RequestParam double amount, @RequestParam(required = false) String description) {
        return txnService.deposit(accountNo, amount, description);
    }

    @PostMapping("/withdraw")
    public String withdrawMoney(@RequestParam Long accountNo, @RequestParam double amount, @RequestParam(required = false) String description) {
        return txnService.withdraw(accountNo, amount, description);
    }

    @PostMapping("/transfer")
    public String transferMoney(@RequestParam Long fromAccNo, @RequestParam Long toAccNo, @RequestParam double amount, @RequestParam(required = false) String description) {
        return txnService.transfer(fromAccNo, toAccNo, amount, description);
    }

    // Fixed Deposit

    @PostMapping("/create-fd")
    public String createFixedDeposit(@RequestBody FixedDeposit fd) {
        return fdService.createFixedDeposit(fd);
    }
    
    // Get all FDs
    @GetMapping("/fds")
    public List<FixedDeposit> getAllFDs() {
        return fdService.getAllFDs();
    }

    // Get FDs by account
    @GetMapping("/fd/account/{accountNo}")
    public List<FixedDeposit> getFDsByAccount(@PathVariable Long accountNo) {
        return fdService.getAllFDsByAccount(accountNo);
    }

    // Close FD
    @PutMapping("/fd/close/{fdId}")
    public String closeFD(@PathVariable Long fdId) {
        boolean result = fdDAO.closeFD(fdId);
        return result ? "FD Closed Successfully" : "FD Not Found";
    }
    @PutMapping("/fd/update/{fdId}")
    public String updateFD(@PathVariable Long fdId, @RequestBody FixedDeposit fd){
        return fdService.updateFD(fdId, fd);
    }
    @PutMapping("/fd/check-mature/{fdId}")
    public String checkAndMature(@PathVariable Long fdId) {
        return fdService.checkAndMatureFD(fdId);
    }

    // Delete
    @DeleteMapping("account/{accountNo}")
    public String deleteAccount(@PathVariable Long accountNo){
        if(accService.deleteAccount(accountNo)){
            return "Account Deleted Sucessfully";
        }else return "Account not Found";
    }

}
