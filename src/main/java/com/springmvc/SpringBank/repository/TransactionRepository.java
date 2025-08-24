package com.springmvc.SpringBank.repository;

import com.springmvc.SpringBank.entity.Transaction;
import com.springmvc.SpringBank.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Transaction entity
 * Provides data access methods for transaction operations
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * Find all transactions for a specific account, ordered by transaction date (newest first)
     * Used for transaction history inquiries
     */
    List<Transaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber);
    
    /**
     * Find transactions by transaction type
     * Useful for reporting and analysis
     */
    List<Transaction> findByTransactionType(TransactionType transactionType);
    
    /**
     * Find transactions by account number and transaction type
     * Useful for specific transaction type analysis per account
     */
    List<Transaction> findByAccountNumberAndTransactionTypeOrderByTransactionDateDesc(
        String accountNumber, TransactionType transactionType);
    
    /**
     * Find transactions within a date range for a specific account
     * Useful for periodic reporting
     */
    @Query("SELECT t FROM Transaction t WHERE t.accountNumber = :accountNumber " +
           "AND t.transactionDate >= :startDate AND t.transactionDate <= :endDate " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionsByAccountAndDateRange(
        @Param("accountNumber") String accountNumber, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find recent transactions for an account (last N transactions)
     * Note: LIMIT is not standard JPQL, so we'll use a different approach in service layer
     */
    List<Transaction> findTop10ByAccountNumberOrderByTransactionDateDesc(String accountNumber);
    
    /**
     * Count total transactions for an account
     */
    long countByAccountNumber(String accountNumber);
    
    /**
     * Check if any transactions exist for an account
     * Useful before closing an account
     */
    boolean existsByAccountNumber(String accountNumber);
}