package com.springmvc.SpringBank.dto;

import com.springmvc.SpringBank.entity.Transaction;
import com.springmvc.SpringBank.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Transaction responses
 * Returns transaction details after successful operations
 */
public class TransactionResponse {
    
    private Long transactionId;
    private String accountNumber;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime transactionDate;
    
    // Default constructor
    public TransactionResponse() {}
    
    // Constructor with all fields
    public TransactionResponse(Long transactionId, String accountNumber, TransactionType transactionType, 
                             BigDecimal amount, BigDecimal balanceAfter, String description, 
                             LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.transactionDate = transactionDate;
    }
    
    /**
     * Static factory method to create TransactionResponse from Transaction entity
     */
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
            transaction.getId(),
            transaction.getAccountNumber(),
            transaction.getTransactionType(),
            transaction.getAmount(),
            transaction.getBalanceAfter(),
            transaction.getDescription(),
            transaction.getTransactionDate()
        );
    }
    
    // Getters and Setters
    public Long getTransactionId() { 
        return transactionId; 
    }
    
    public void setTransactionId(Long transactionId) { 
        this.transactionId = transactionId; 
    }
    
    public String getAccountNumber() { 
        return accountNumber; 
    }
    
    public void setAccountNumber(String accountNumber) { 
        this.accountNumber = accountNumber; 
    }
    
    public TransactionType getTransactionType() { 
        return transactionType; 
    }
    
    public void setTransactionType(TransactionType transactionType) { 
        this.transactionType = transactionType; 
    }
    
    public BigDecimal getAmount() { 
        return amount; 
    }
    
    public void setAmount(BigDecimal amount) { 
        this.amount = amount; 
    }
    
    public BigDecimal getBalanceAfter() { 
        return balanceAfter; 
    }
    
    public void setBalanceAfter(BigDecimal balanceAfter) { 
        this.balanceAfter = balanceAfter; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public LocalDateTime getTransactionDate() { 
        return transactionDate; 
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) { 
        this.transactionDate = transactionDate; 
    }
    
    @Override
    public String toString() {
        return "TransactionResponse{" +
                "transactionId=" + transactionId +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", description='" + description + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}