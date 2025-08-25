package com.springmvc.SpringBank.repository;

import com.springmvc.SpringBank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.accounts WHERE c.id = :id")
    Optional<Customer> findByIdWithAccounts(@Param("id") Long id);
    
    boolean existsByEmail(String email);
    // Add this method to your existing CustomerRepository.java interface

/**
 * Find customers by name containing search term (case-insensitive)
 * Spring Data JPA will auto-implement this method
 */
   List<Customer> findByNameContainingIgnoreCase(String name);
}