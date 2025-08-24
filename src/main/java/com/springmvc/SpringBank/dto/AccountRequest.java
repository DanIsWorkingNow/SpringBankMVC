package com.springmvc.SpringBank.dto;

import com.springmvc.SpringBank.enums.AccountType;
import jakarta.validation.constraints.NotNull;

public class AccountRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    // Default constructor
    public AccountRequest() {}
    
    // Constructor
    public AccountRequest(Long customerId, AccountType accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
    }
    
    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    
    @Override
    public String toString() {
        return "AccountRequest{" +
                "customerId=" + customerId +
                ", accountType=" + accountType +
                '}';
    }
}