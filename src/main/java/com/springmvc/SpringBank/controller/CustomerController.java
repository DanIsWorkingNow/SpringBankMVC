package com.springmvc.SpringBank.controller;

import com.springmvc.SpringBank.dto.CustomerRequest;
import com.springmvc.SpringBank.dto.CustomerResponse;
import com.springmvc.SpringBank.entity.Customer;
import com.springmvc.SpringBank.exception.CustomerNotFoundException;
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
 * FIXES: Added missing import java.util.List and missing @GetMapping endpoints
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class CustomerController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * CREATE CUSTOMER API
     * Assessment Requirement: "Create Customer: Accepts name and auto-generates ID"
     * POST /api/customers
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        logger.info("Received request to create customer: {}", request.getName());
        
        try {
            Customer customer = customerService.createCustomer(
                request.getName(), 
                request.getEmail(), 
                request.getPhone()
            );
            
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
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for customer creation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            
        } catch (Exception e) {
            logger.error("Unexpected error creating customer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET CUSTOMER BY ID API
     * Assessment Requirement: "Inquire Customer: Returns customer details by ID"
     * GET /api/customers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        logger.info("Received request to get customer by ID: {}", id);
        
        try {
            Customer customer = customerService.findCustomerById(id);
            
            CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedDate()
            );
            
            logger.info("Found customer: {} | ID: {}", customer.getName(), id);
            return ResponseEntity.ok(response);
            
        } catch (CustomerNotFoundException e) {
            logger.warn("Customer not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            
        } catch (Exception e) {
            logger.error("Unexpected error getting customer by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET ALL CUSTOMERS API - CRITICAL MISSING ENDPOINT
     * This was causing the 405 Method Not Allowed error in frontend
     * Frontend connectivity testing requires this endpoint
     * GET /api/customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        logger.info("Received request to get all customers");
        
        try {
            List<Customer> customers = customerService.findAllCustomers();
            
            List<CustomerResponse> responseList = customers.stream()
                .map(customer -> new CustomerResponse(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getCreatedDate()
                ))
                .collect(Collectors.toList());
            
            logger.info("Found {} customers in database", responseList.size());
            return ResponseEntity.ok(responseList);
            
        } catch (Exception e) {
            logger.error("Unexpected error retrieving all customers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * SEARCH CUSTOMERS BY NAME API - Enhancement
     * Improves user experience for customer lookup
     * GET /api/customers/search?name={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> searchCustomersByName(@RequestParam String name) {
        logger.info("Received request to search customers by name: {}", name);
        
        try {
            if (name == null || name.trim().isEmpty()) {
                logger.warn("Search name parameter is empty or null");
                return ResponseEntity.badRequest().build();
            }
            
            List<Customer> customers = customerService.findCustomersByName(name.trim());
            
            List<CustomerResponse> responseList = customers.stream()
                .map(customer -> new CustomerResponse(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getCreatedDate()
                ))
                .collect(Collectors.toList());
            
            logger.info("Found {} customers matching name: '{}'", responseList.size(), name);
            return ResponseEntity.ok(responseList);
            
        } catch (Exception e) {
            logger.error("Unexpected error searching customers by name: {}", name, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET CUSTOMER COUNT API - Utility
     * Dashboard and admin functionality
     * GET /api/customers/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCustomerCount() {
        logger.info("Received request to get customer count");
        
        try {
            long count = customerService.getCustomerCount();
            
            logger.info("Total customer count: {}", count);
            return ResponseEntity.ok(count);
            
        } catch (Exception e) {
            logger.error("Unexpected error getting customer count", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}