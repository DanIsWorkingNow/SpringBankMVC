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

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    @Autowired
    private CustomerService customerService;
    
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        logger.info("Received request to create customer: {}", request.getName());
        
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
        
        logger.info("Customer created successfully with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        logger.info("Received request to get customer with ID: {}", id);
        
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
        
        logger.info("Customer found: {}", response.getName());
        return ResponseEntity.ok(response);
    }
}