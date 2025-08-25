package com.springmvc.SpringBank.service;

import com.springmvc.SpringBank.entity.Customer;
import com.springmvc.SpringBank.exception.CustomerNotFoundException;
import com.springmvc.SpringBank.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Customer management
 * Contains business logic for customer operations
 * FIXES: Added missing import java.util.List and implemented missing methods
 */
@Service
@Transactional
public class CustomerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    @Autowired
    private CustomerRepository customerRepository;
    
    /**
     * Create a new customer
     * Assessment Requirement: "Create Customer: Accepts name and auto-generates ID"
     */
    public Customer createCustomer(String name, String email, String phone) {
        logger.info("Creating customer with name: {}", name);
        
        // Validate email uniqueness
        if (email != null && customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        
        Customer customer = new Customer(name, email, phone);
        Customer savedCustomer = customerRepository.save(customer);
        
        logger.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return savedCustomer;
    }
    
    /**
     * Find customer by ID
     * Assessment Requirement: "Inquire Customer: Returns customer details by ID"
     */
    @Transactional(readOnly = true)
    public Customer findCustomerById(Long id) {
        logger.info("Searching for customer with ID: {}", id);
        
        return customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
    }

    /**
     * GET ALL CUSTOMERS - CRITICAL MISSING METHOD
     * Returns all customers in the database
     * This method was missing and causing compilation errors
     * Frontend requires this for the GET /api/customers endpoint
     */
    @Transactional(readOnly = true)
    public List<Customer> findAllCustomers() {
        logger.info("Retrieving all customers from database");
        
        try {
            List<Customer> customers = customerRepository.findAll();
            
            logger.info("Found {} customers in database", customers.size());
            return customers;
            
        } catch (Exception e) {
            logger.error("Error retrieving all customers from database", e);
            throw new RuntimeException("Failed to retrieve customers", e);
        }
    }

    /**
     * SEARCH CUSTOMERS BY NAME - MISSING METHOD
     * Returns customers whose names contain the search term (case-insensitive)
     * This method was referenced but not implemented, causing compilation errors
     */
    @Transactional(readOnly = true)
    public List<Customer> findCustomersByName(String name) {
        logger.info("Searching customers by name containing: {}", name);
        
        try {
            List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
            
            logger.info("Found {} customers matching name pattern: '{}'", customers.size(), name);
            return customers;
            
        } catch (Exception e) {
            logger.error("Error searching customers by name: {}", name, e);
            throw new RuntimeException("Failed to search customers by name", e);
        }
    }

    /**
     * GET CUSTOMER COUNT - MISSING METHOD
     * Returns total number of customers in the database
     * This method was referenced but not implemented, causing compilation errors
     */
    @Transactional(readOnly = true)
    public long getCustomerCount() {
        logger.info("Getting total customer count");
        
        try {
            long count = customerRepository.count();
            
            logger.info("Total customer count: {}", count);
            return count;
            
        } catch (Exception e) {
            logger.error("Error getting customer count from database", e);
            throw new RuntimeException("Failed to get customer count", e);
        }
    }
}