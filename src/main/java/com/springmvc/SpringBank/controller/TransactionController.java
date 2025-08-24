package com.springmvc.SpringBank.controller;

import com.springmvc.SpringBank.dto.TransactionRequest;
import com.springmvc.SpringBank.dto.TransactionResponse;
import com.springmvc.SpringBank.entity.Transaction;
import com.springmvc.SpringBank.enums.TransactionType;
import com.springmvc.SpringBank.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Transaction operations
 * Handles deposit, withdrawal, and transaction history endpoints
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    
    @Autowired
    private TransactionService transactionService;
    
    /**
     * DEPOSIT CASH API
     * Assessment Requirement: "Deposit Cash: Accepts account number and deposit amount, and updates the balance."
     * 
     * POST /api/transactions/deposit
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        logger.info("Received deposit request: {} to account: {}", 
                   request.getAmount(), request.getAccountNumber());
        
        try {
            Transaction transaction = transactionService.deposit(
                request.getAccountNumber(),
                request.getAmount(),
                request.getDescription()
            );
            
            TransactionResponse response = TransactionResponse.from(transaction);
            
            logger.info("Deposit completed successfully: Transaction ID {} | Amount: {} | New Balance: {}", 
                       response.getTransactionId(), response.getAmount(), response.getBalanceAfter());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing deposit for account: {} | Amount: {}", 
                        request.getAccountNumber(), request.getAmount(), e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * WITHDRAW CASH API  
     * Assessment Requirement: "Withdraw Cash: Accepts account number and withdrawal amount, and updates the balance."
     * 
     * POST /api/transactions/withdraw
     */
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        logger.info("Received withdrawal request: {} from account: {}", 
                   request.getAmount(), request.getAccountNumber());
        
        try {
            Transaction transaction = transactionService.withdraw(
                request.getAccountNumber(),
                request.getAmount(),
                request.getDescription()
            );
            
            TransactionResponse response = TransactionResponse.from(transaction);
            
            logger.info("Withdrawal completed successfully: Transaction ID {} | Amount: {} | New Balance: {}", 
                       response.getTransactionId(), response.getAmount(), response.getBalanceAfter());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing withdrawal for account: {} | Amount: {}", 
                        request.getAccountNumber(), request.getAmount(), e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * GET TRANSACTION HISTORY
     * Returns all transactions for an account
     * 
     * GET /api/transactions/account/{accountNumber}
     */
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable String accountNumber) {
        logger.info("Received transaction history request for account: {}", accountNumber);
        
        try {
            List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
            
            List<TransactionResponse> responses = transactions.stream()
                .map(TransactionResponse::from)
                .collect(Collectors.toList());
            
            logger.info("Retrieved {} transactions for account: {}", responses.size(), accountNumber);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("Error retrieving transaction history for account: {}", accountNumber, e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * GET RECENT TRANSACTIONS
     * Returns last 10 transactions for an account
     * 
     * GET /api/transactions/account/{accountNumber}/recent
     */
    @GetMapping("/account/{accountNumber}/recent")
    public ResponseEntity<List<TransactionResponse>> getRecentTransactions(@PathVariable String accountNumber) {
        logger.info("Received recent transactions request for account: {}", accountNumber);
        
        try {
            List<Transaction> transactions = transactionService.getRecentTransactions(accountNumber);
            
            List<TransactionResponse> responses = transactions.stream()
                .map(TransactionResponse::from)
                .collect(Collectors.toList());
            
            logger.info("Retrieved {} recent transactions for account: {}", responses.size(), accountNumber);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("Error retrieving recent transactions for account: {}", accountNumber, e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * GET TRANSACTIONS BY TYPE
     * Returns transactions of a specific type for an account
     * 
     * GET /api/transactions/account/{accountNumber}/type/{transactionType}
     */
    @GetMapping("/account/{accountNumber}/type/{transactionType}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByType(
            @PathVariable String accountNumber, 
            @PathVariable TransactionType transactionType) {
        
        logger.info("Received {} transactions request for account: {}", transactionType, accountNumber);
        
        try {
            List<Transaction> transactions = transactionService.getTransactionsByType(accountNumber, transactionType);
            
            List<TransactionResponse> responses = transactions.stream()
                .map(TransactionResponse::from)
                .collect(Collectors.toList());
            
            logger.info("Retrieved {} {} transactions for account: {}", 
                       responses.size(), transactionType, accountNumber);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("Error retrieving {} transactions for account: {}", transactionType, accountNumber, e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * GET TRANSACTION COUNT
     * Returns total number of transactions for an account
     * 
     * GET /api/transactions/account/{accountNumber}/count
     */
    @GetMapping("/account/{accountNumber}/count")
    public ResponseEntity<Long> getTransactionCount(@PathVariable String accountNumber) {
        logger.info("Received transaction count request for account: {}", accountNumber);
        
        try {
            long count = transactionService.getTransactionCount(accountNumber);
            
            logger.info("Account {} has {} total transactions", accountNumber, count);
            return ResponseEntity.ok(count);
            
        } catch (Exception e) {
            logger.error("Error retrieving transaction count for account: {}", accountNumber, e);
            throw e; // Let global exception handler deal with it
        }
    }
}