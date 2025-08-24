package com.springmvc.SpringBank.dto;

import java.time.LocalDateTime;

public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdDate;
    
    // Default constructor
    public CustomerResponse() {}
    
    // Constructor with all fields
    public CustomerResponse(Long id, String name, String email, String phone, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdDate = createdDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}