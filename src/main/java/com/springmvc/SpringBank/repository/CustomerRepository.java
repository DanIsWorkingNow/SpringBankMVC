package com.springmvc.SpringBank.repository;

import com.springmvc.SpringBank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entity
 * Provides database access methods for customer operations
 * FIXES: Added missing import java.util.List and missing query method
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customer by email address
     * Used for email uniqueness validation
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find customer with their accounts using LEFT JOIN FETCH for performance
     * Avoids N+1 query problem when loading customer with accounts
     */
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.accounts WHERE c.id = :id")
    Optional<Customer> findByIdWithAccounts(@Param("id") Long id);
    
    /**
     * Check if customer exists by email
     * Used for validation during customer creation
     */
    boolean existsByEmail(String email);
    
    /**
     * CRITICAL MISSING METHOD - Find customers by name containing search term (case-insensitive)
     * Spring Data JPA will auto-implement this method based on method name
     * This method was referenced in CustomerService but was missing, causing compilation errors
     * 
     * Method naming convention:
     * - findBy: Query prefix
     * - Name: Entity field to search
     * - Containing: SQL LIKE operation with % wildcards
     * - IgnoreCase: Case-insensitive search
     * 
     * Generated SQL: SELECT * FROM customers WHERE UPPER(name) LIKE UPPER('%searchTerm%')
     */
    List<Customer> findByNameContainingIgnoreCase(String name);
}