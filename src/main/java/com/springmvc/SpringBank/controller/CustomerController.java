package com.springmvc.SpringBank.controller;

import com.springmvc.SpringBank.dto.CustomerRequest;
import com.springmvc.SpringBank.dto.CustomerResponse;
import com.springmvc.SpringBank.entity.Customer;
import com.springmvc.SpringBank.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Customer management
 * Handles all customer-related HTTP requests
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * CREATE CUSTOMER API
     * Assessment Requirement: "Create Customer: Accepts name and auto-generates ID"
     * 
     * POST /api/customers
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        logger.info("Received request to create customer: {}", request.getName());
        
        try {
            // Create customer using service
            Customer customer = customerService.createCustomer(
                request.getName(), 
                request.getEmail(), 
                request.getPhone()
            );
            
            // Convert to response DTO
            CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedDate()
            );
            
            logger.info("Customer created successfully with ID: {} | Name: {}", 
                       response.getId(), response.getName());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            logger.error("Error creating customer: {}", request.getName(), e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * INQUIRE CUSTOMER API (by ID)
     * Assessment Requirement: "Inquire Customer: Returns details by customer ID"
     * 
     * GET /api/customers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        logger.info("Received request to get customer with ID: {}", id);
        
        try {
            // Find customer using service
            Customer customer = customerService.findCustomerById(id);
            
            // Convert to response DTO
            CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedDate()
            );
            
            logger.info("Customer found: {} (ID: {})", response.getName(), response.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving customer with ID: {}", id, e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * GET ALL CUSTOMERS API
     * **THIS WAS MISSING** - Needed for frontend connectivity testing and admin functions
     * 
     * GET /api/customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        logger.info("Received request to get all customers");
        
        try {
            // Get all customers using service
            List<Customer> customers = customerService.findAllCustomers();
            
            // Convert to response DTOs
            List<CustomerResponse> responses = customers.stream()
                .map(customer -> new CustomerResponse(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getCreatedDate()
                ))
                .collect(Collectors.toList());
            
            logger.info("Retrieved {} customers successfully", responses.size());
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("Error retrieving all customers", e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * SEARCH CUSTOMERS BY NAME API
     * Additional utility endpoint for better user experience
     * 
     * GET /api/customers/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> searchCustomersByName(@RequestParam String name) {
        logger.info("Received request to search customers by name: {}", name);
        
        try {
            // Search customers by name using service
            List<Customer> customers = customerService.findCustomersByName(name);
            
            // Convert to response DTOs
            List<CustomerResponse> responses = customers.stream()
                .map(customer -> new CustomerResponse(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getCreatedDate()
                ))
                .collect(Collectors.toList());
            
            logger.info("Found {} customers matching name: '{}'", responses.size(), name);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            logger.error("Error searching customers by name: {}", name, e);
            throw e; // Let global exception handler deal with it
        }
    }
    
    /**
     * GET CUSTOMER COUNT API
     * Utility endpoint for dashboard/admin purposes
     * 
     * GET /api/customers/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCustomerCount() {
        logger.info("Received request to get customer count");
        
        try {
            long count = customerService.getCustomerCount();
            
            logger.info("Total customers in database: {}", count);
            return ResponseEntity.ok(count);
            
        } catch (Exception e) {
            logger.error("Error retrieving customer count", e);
            throw e; // Let global exception handler deal with it
        }
    }
}