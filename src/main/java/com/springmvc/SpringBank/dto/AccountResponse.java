package com.springmvc.SpringBank.dto;

import com.springmvc.SpringBank.enums.AccountStatus;
import com.springmvc.SpringBank.enums.AccountType;
import com.springmvc.SpringBank.entity.Account;
import com.springmvc.SpringBank.entity.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {
    private String accountNumber;
    private Long customerId;
    private String customerName;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime createdDate;
    
    // Default constructor
    public AccountResponse() {}
    
    // Constructor with all fields
    public AccountResponse(String accountNumber, Long customerId, String customerName, 
                          AccountType accountType, BigDecimal balance, AccountStatus status, 
                          LocalDateTime createdDate) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.createdDate = createdDate;
    }
    
    // Static factory method to create from Account and Customer entities
    public static AccountResponse from(Account account, Customer customer) {
        return new AccountResponse(
            account.getAccountNumber(),
            account.getCustomerId(),
            customer.getName(),
            account.getAccountType(),
            account.getBalance(),
            account.getStatus(),
            account.getCreatedDate()
        );
    }
    
    // Static factory method when customer is already part of account
    public static AccountResponse from(Account account) {
        return new AccountResponse(
            account.getAccountNumber(),
            account.getCustomerId(),
            account.getCustomer() != null ? account.getCustomer().getName() : null,
            account.getAccountType(),
            account.getBalance(),
            account.getStatus(),
            account.getCreatedDate()
        );
    }
    
    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    @Override
    public String toString() {
        return "AccountResponse{" +
                "accountNumber='" + accountNumber + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", status=" + status +
                ", createdDate=" + createdDate +
                '}';
    }
}