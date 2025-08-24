package com.springmvc.SpringBank.service;

import com.springmvc.SpringBank.entity.Account;
import com.springmvc.SpringBank.entity.Customer;
import com.springmvc.SpringBank.enums.AccountStatus;
import com.springmvc.SpringBank.enums.AccountType;
import com.springmvc.SpringBank.exception.AccountNotFoundException;
import com.springmvc.SpringBank.exception.CustomerNotFoundException;
import com.springmvc.SpringBank.repository.AccountRepository;
import com.springmvc.SpringBank.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class AccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    /**
     * Create a new account for a customer
     * Assessment Requirement: "Create Account: Accepts account type, and creates an account 
     * with an auto-generated number. Account status should be set as "Active"."
     */
    public Account createAccount(Long customerId, AccountType accountType) {
        logger.info("Creating account for customer ID: {}, type: {}", customerId, accountType);
        
        // Validate customer exists
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        
        // Generate unique account number
        String accountNumber = generateUniqueAccountNumber();
        
        // Create account with ACTIVE status (assessment requirement)
        Account account = new Account(accountNumber, customerId, accountType);
        account.setStatus(AccountStatus.ACTIVE); // Explicitly set as required
        
        Account savedAccount = accountRepository.save(account);
        
        logger.info("Account created successfully: {} for customer: {}", accountNumber, customer.getName());
        return savedAccount;
    }
    
    /**
     * Find account by account number
     * Assessment Requirement: "Inquire Account: Accepts account number and returns the details 
     * of the account holder and the account status."
     */
    @Transactional(readOnly = true)
    public Account findByAccountNumber(String accountNumber) {
        logger.info("Finding account: {}", accountNumber);
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        
        logger.info("Account found: {} for customer ID: {}", accountNumber, account.getCustomerId());
        return account;
    }
    
    /**
     * REMOVED PROBLEMATIC METHOD - using simple findByAccountNumber instead
     * The controller will handle getting customer details separately
     */
    
    /**
     * Close an account
     * Assessment Requirement: "Close Account: Accepts account number and updates the status to "Closed"."
     */
    public Account closeAccount(String accountNumber) {
        logger.info("Closing account: {}", accountNumber);
        
        Account account = findByAccountNumber(accountNumber);
        
        // Business rule validations
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException("Account is already closed: " + accountNumber);
        }
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            logger.warn("Attempting to close account with positive balance: {} (Balance: {})", 
                       accountNumber, account.getBalance());
            throw new IllegalStateException(
                "Cannot close account with positive balance. Current balance: " + account.getBalance()
            );
        }
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            logger.warn("Attempting to close account with negative balance: {} (Balance: {})", 
                       accountNumber, account.getBalance());
            throw new IllegalStateException(
                "Cannot close account with negative balance. Current balance: " + account.getBalance()
            );
        }
        
        // Update status to CLOSED as required
        account.setStatus(AccountStatus.CLOSED);
        Account savedAccount = accountRepository.save(account);
        
        logger.info("Account closed successfully: {}", accountNumber);
        return savedAccount;
    }
    
    /**
     * Find all accounts for a customer
     */
    @Transactional(readOnly = true)
    public List<Account> findAccountsByCustomerId(Long customerId) {
        logger.info("Finding accounts for customer ID: {}", customerId);
        
        // Validate customer exists
        customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        logger.info("Found {} accounts for customer ID: {}", accounts.size(), customerId);
        
        return accounts;
    }
    
    /**
     * Find active accounts for a customer
     */
    @Transactional(readOnly = true)
    public List<Account> findActiveAccountsByCustomerId(Long customerId) {
        logger.info("Finding active accounts for customer ID: {}", customerId);
        
        List<Account> activeAccounts = accountRepository.findActiveAccountsByCustomerId(customerId);
        logger.info("Found {} active accounts for customer ID: {}", activeAccounts.size(), customerId);
        
        return activeAccounts;
    }
    
    /**
     * Update account balance - used by TransactionService
     */
    public Account updateBalance(String accountNumber, BigDecimal newBalance) {
        logger.info("Updating balance for account: {} to {}", accountNumber, newBalance);
        
        Account account = findByAccountNumber(accountNumber);
        
        // Validate account is active
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot update balance for non-active account: " + accountNumber);
        }
        
        account.setBalance(newBalance);
        Account savedAccount = accountRepository.save(account);
        
        logger.info("Balance updated successfully for account: {}", accountNumber);
        return savedAccount;
    }
    
    /**
     * Generate unique account number
     * Format: ACC + timestamp + 3-digit random number
     */
    private String generateUniqueAccountNumber() {
        String accountNumber;
        int attempts = 0;
        int maxAttempts = 10;
        
        do {
            accountNumber = "ACC" + System.currentTimeMillis() + 
                           String.format("%03d", new Random().nextInt(1000));
            attempts++;
            
            if (attempts > maxAttempts) {
                logger.error("Failed to generate unique account number after {} attempts", maxAttempts);
                throw new RuntimeException("Unable to generate unique account number");
            }
            
        } while (accountRepository.existsByAccountNumber(accountNumber));
        
        logger.debug("Generated unique account number: {} in {} attempts", accountNumber, attempts);
        return accountNumber;
    }
}