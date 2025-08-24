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
    
    // Additional methods...
}