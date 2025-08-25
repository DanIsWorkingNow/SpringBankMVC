package com.springmvc.SpringBank.service;

import com.springmvc.SpringBank.entity.Customer;
import com.springmvc.SpringBank.exception.CustomerNotFoundException;
import com.springmvc.SpringBank.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    @Autowired
    private CustomerRepository customerRepository;
    
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
    
    @Transactional(readOnly = true)
    public Customer findCustomerById(Long id) {
        logger.info("Searching for customer with ID: {}", id);
        
        return customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
    }

    // Add these methods to your existing CustomerService.java

/**
 * GET ALL CUSTOMERS
 * Returns all customers in the database
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
 * SEARCH CUSTOMERS BY NAME
 * Returns customers whose names contain the search term (case-insensitive)
 */
@Transactional(readOnly = true)
public List<Customer> findCustomersByName(String name) {
    logger.info("Searching for customers with name containing: {}", name);
    
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Search name cannot be empty");
    }
    
    try {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name.trim());
        
        logger.info("Found {} customers matching name: '{}'", customers.size(), name);
        return customers;
        
    } catch (Exception e) {
        logger.error("Error searching customers by name: {}", name, e);
        throw new RuntimeException("Failed to search customers", e);
    }
}

/**
 * GET CUSTOMER COUNT
 * Returns total number of customers in database
 */
@Transactional(readOnly = true)
public long getCustomerCount() {
    logger.info("Counting total customers in database");
    
    try {
        long count = customerRepository.count();
        
        logger.info("Total customers in database: {}", count);
        return count;
        
    } catch (Exception e) {
        logger.error("Error counting customers", e);
        throw new RuntimeException("Failed to count customers", e);
    }
}
    
    // Additional methods...
}