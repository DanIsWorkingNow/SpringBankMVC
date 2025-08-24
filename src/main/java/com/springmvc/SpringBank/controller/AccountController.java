package com.springmvc.SpringBank.controller;

import com.springmvc.SpringBank.dto.AccountRequest;
import com.springmvc.SpringBank.dto.AccountResponse;
import com.springmvc.SpringBank.entity.Account;
import com.springmvc.SpringBank.entity.Customer;
import com.springmvc.SpringBank.service.AccountService;
import com.springmvc.SpringBank.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * Create Account API
     * Assessment Requirement: "Create Account: Accepts account type, and creates an account 
     * with an auto-generated number. Account status should be set as "Active"."
     * 
     * POST /api/accounts
     */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        logger.info("Received request to create account for customer: {}, type: {}", 
                   request.getCustomerId(), request.getAccountType());
        
        try {
            // Create account using service
            Account account = accountService.createAccount(
                request.getCustomerId(), 
                request.getAccountType()
            );
            
            // Get customer details for response
            Customer customer = customerService.findCustomerById(account.getCustomerId());
            
            // Convert to response DTO
            AccountResponse response = AccountResponse.from(account, customer);
            
            logger.info("Account created successfully: {} for customer: {}", 
                       response.getAccountNumber(), response.getCustomerName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            logger.error("Error creating account for customer: {}", request.getCustomerId(), e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * Account Inquiry API
     * Assessment Requirement: "Inquire Account: Accepts account number and returns the details 
     * of the account holder and the account status."
     * 
     * GET /api/accounts/{accountNumber}
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        logger.info("Received request to get account: {}", accountNumber);
        
        try {
            // Find account using regular method (not the problematic one)
            Account account = accountService.findByAccountNumber(accountNumber);
            
            // Get customer details separately
            Customer customer = customerService.findCustomerById(account.getCustomerId());
            
            // Convert to response DTO
            AccountResponse response = AccountResponse.from(account, customer);
            
            logger.info("Account found: {} for customer: {} (Status: {})", 
                       response.getAccountNumber(), response.getCustomerName(), response.getStatus());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving account: {}", accountNumber, e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * Close Account API
     * Assessment Requirement: "Close Account: Accepts account number and updates the status to "Closed"."
     * 
     * PUT /api/accounts/{accountNumber}/close
     */
    @PutMapping("/{accountNumber}/close")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable String accountNumber) {
        logger.info("Received request to close account: {}", accountNumber);
        
        try {
            // Close account using service
            Account account = accountService.closeAccount(accountNumber);
            
            // Get customer details
            Customer customer = customerService.findCustomerById(account.getCustomerId());
            
            // Convert to response DTO
            AccountResponse response = AccountResponse.from(account, customer);
            
            logger.info("Account closed successfully: {} for customer: {}", 
                       response.getAccountNumber(), response.getCustomerName());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error closing account: {}", accountNumber, e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * Get all accounts for a customer
     * Additional utility endpoint
     * 
     * GET /api/accounts/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByCustomer(@PathVariable Long customerId) {
        logger.info("Received request to get accounts for customer: {}", customerId);
        
        try {
            // Find customer first (validates existence)
            Customer customer = customerService.findCustomerById(customerId);
            
            // Get all accounts for customer
            List<Account> accounts = accountService.findAccountsByCustomerId(customerId);
            
            // Convert to response DTOs
            List<AccountResponse> responses = accounts.stream()
                .map(account -> AccountResponse.from(account, customer))
                .collect(Collectors.toList());
            
            logger.info("Found {} accounts for customer: {} ({})", 
                       responses.size(), customer.getName(), customerId);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("Error retrieving accounts for customer: {}", customerId, e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * Get active accounts for a customer
     * Additional utility endpoint
     * 
     * GET /api/accounts/customer/{customerId}/active
     */
    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<List<AccountResponse>> getActiveAccountsByCustomer(@PathVariable Long customerId) {
        logger.info("Received request to get active accounts for customer: {}", customerId);
        
        try {
            // Find customer first (validates existence)
            Customer customer = customerService.findCustomerById(customerId);
            
            // Get active accounts for customer
            List<Account> accounts = accountService.findActiveAccountsByCustomerId(customerId);
            
            // Convert to response DTOs
            List<AccountResponse> responses = accounts.stream()
                .map(account -> AccountResponse.from(account, customer))
                .collect(Collectors.toList());
            
            logger.info("Found {} active accounts for customer: {} ({})", 
                       responses.size(), customer.getName(), customerId);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("Error retrieving active accounts for customer: {}", customerId, e);
            throw e; // Let global exception handler deal with it
        }
    }
}