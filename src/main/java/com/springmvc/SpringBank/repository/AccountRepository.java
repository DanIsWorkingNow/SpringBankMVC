package com.springmvc.SpringBank.repository;

import com.springmvc.SpringBank.entity.Account;
import com.springmvc.SpringBank.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    // Find accounts by customer ID
    List<Account> findByCustomerId(Long customerId);
    
    // Find account by account number
    Optional<Account> findByAccountNumber(String accountNumber);
    
    // Find accounts by status
    List<Account> findByStatus(AccountStatus status);
    
    // Find active accounts for a customer
    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.status = 'ACTIVE'")
    List<Account> findActiveAccountsByCustomerId(@Param("customerId") Long customerId);
    
    // Check if account number exists
    boolean existsByAccountNumber(String accountNumber);
    
    // REMOVED PROBLEMATIC JOIN FETCH - using separate queries instead
    // The original query caused issues with the read-only relationship
}