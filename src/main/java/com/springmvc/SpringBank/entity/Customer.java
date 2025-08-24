package com.springmvc.SpringBank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;
    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(unique = true, length = 100)
    private String email;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(length = 20)
    private String phone;
    
    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();
    
    // Default constructor
    public Customer() {}
    
    // Constructor for creation
    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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

   public LocalDateTime getUpdatedDate() { return updatedDate; }
   public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

   public List<Account> getAccounts() { return accounts; }
   public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
}