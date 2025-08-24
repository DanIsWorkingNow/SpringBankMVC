package com.springmvc.SpringBank.service;

import com.springmvc.SpringBank.entity.Account;
import com.springmvc.SpringBank.entity.Transaction;
import com.springmvc.SpringBank.enums.AccountStatus;
import com.springmvc.SpringBank.enums.TransactionType;
import com.springmvc.SpringBank.exception.InsufficientFundsException;
import com.springmvc.SpringBank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Transaction management
 * Handles deposit, withdrawal, and transaction history operations
 */
@Service
@Transactional
public class TransactionService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountService accountService;
    
    /**
     * ASSESSMENT REQUIREMENT 6: Deposit Cash
     * "Accepts account number and deposit amount, and updates the balance"
     */
    public Transaction deposit(String accountNumber, BigDecimal amount, String description) {
        logger.info("Processing deposit: {} to account: {}", amount, accountNumber);
        
        // Validate input parameters
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        
        // Validate account exists and is active
        Account account = accountService.findByAccountNumber(accountNumber);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot deposit to non-active account: " + accountNumber);
        }
        
        // Calculate new balance
        BigDecimal newBalance = account.getBalance().add(amount);
        
        // Update account balance
        accountService.updateBalance(accountNumber, newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            accountNumber,
            TransactionType.DEPOSIT,
            amount,
            newBalance,
            description != null ? description : "Cash deposit"
        );
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        logger.info("Deposit completed successfully: {} | Account: {} | New balance: {}", 
                   savedTransaction.getId(), accountNumber, newBalance);
        return savedTransaction;
    }
    
    /**
     * ASSESSMENT REQUIREMENT 7: Withdraw Cash
     * "Accepts account number and withdrawal amount, and updates the balance"
     */
    public Transaction withdraw(String accountNumber, BigDecimal amount, String description) {
        logger.info("Processing withdrawal: {} from account: {}", amount, accountNumber);
        
        // Validate input parameters
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        
        // Validate account exists and is active
        Account account = accountService.findByAccountNumber(accountNumber);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot withdraw from non-active account: " + accountNumber);
        }
        
        // Check sufficient funds
        if (account.getBalance().compareTo(amount) < 0) {
            logger.warn("Insufficient funds for withdrawal: Account: {} | Available: {} | Requested: {}", 
                       accountNumber, account.getBalance(), amount);
            throw new InsufficientFundsException(
                "Insufficient balance. Available: " + account.getBalance() + ", Requested: " + amount
            );
        }
        
        // Calculate new balance
        BigDecimal newBalance = account.getBalance().subtract(amount);
        
        // Update account balance
        accountService.updateBalance(accountNumber, newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            accountNumber,
            TransactionType.WITHDRAWAL,
            amount,
            newBalance,
            description != null ? description : "Cash withdrawal"
        );
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        logger.info("Withdrawal completed successfully: {} | Account: {} | New balance: {}", 
                   savedTransaction.getId(), accountNumber, newBalance);
        return savedTransaction;
    }
    
    /**
     * Get transaction history for an account
     * Returns all transactions ordered by date (newest first)
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(String accountNumber) {
        logger.info("Retrieving transaction history for account: {}", accountNumber);
        
        // Validate account exists
        accountService.findByAccountNumber(accountNumber);
        
        List<Transaction> transactions = transactionRepository
            .findByAccountNumberOrderByTransactionDateDesc(accountNumber);
        
        logger.info("Found {} transactions for account: {}", transactions.size(), accountNumber);
        return transactions;
    }
    
    /**
     * Get recent transactions for an account (last 10)
     */
    @Transactional(readOnly = true)
    public List<Transaction> getRecentTransactions(String accountNumber) {
        logger.info("Retrieving recent transactions for account: {}", accountNumber);
        
        // Validate account exists
        accountService.findByAccountNumber(accountNumber);
        
        List<Transaction> transactions = transactionRepository
            .findTop10ByAccountNumberOrderByTransactionDateDesc(accountNumber);
        
        logger.info("Found {} recent transactions for account: {}", transactions.size(), accountNumber);
        return transactions;
    }
    
    /**
     * Get transactions by type for an account
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByType(String accountNumber, TransactionType transactionType) {
        logger.info("Retrieving {} transactions for account: {}", transactionType, accountNumber);
        
        // Validate account exists
        accountService.findByAccountNumber(accountNumber);
        
        List<Transaction> transactions = transactionRepository
            .findByAccountNumberAndTransactionTypeOrderByTransactionDateDesc(accountNumber, transactionType);
        
        logger.info("Found {} {} transactions for account: {}", 
                   transactions.size(), transactionType, accountNumber);
        return transactions;
    }
    
    /**
     * Get transactions within a date range
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateRange(String accountNumber, 
                                                       LocalDateTime startDate, 
                                                       LocalDateTime endDate) {
        logger.info("Retrieving transactions for account: {} between {} and {}", 
                   accountNumber, startDate, endDate);
        
        // Validate account exists
        accountService.findByAccountNumber(accountNumber);
        
        List<Transaction> transactions = transactionRepository
            .findTransactionsByAccountAndDateRange(accountNumber, startDate, endDate);
        
        logger.info("Found {} transactions for account: {} in date range", 
                   transactions.size(), accountNumber);
        return transactions;
    }
    
    /**
     * Get transaction count for an account
     */
    @Transactional(readOnly = true)
    public long getTransactionCount(String accountNumber) {
        // Validate account exists
        accountService.findByAccountNumber(accountNumber);
        
        return transactionRepository.countByAccountNumber(accountNumber);
    }
}