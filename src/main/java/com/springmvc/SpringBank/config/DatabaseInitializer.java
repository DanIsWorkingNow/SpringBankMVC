package com.springmvc.SpringBank.config;

import com.springmvc.SpringBank.entity.Customer;
import com.springmvc.SpringBank.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Database Initialization Component
 * Creates sample customers on application startup for testing purposes
 * This solves the H2 refresh issue by auto-populating data
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("🚀 Initializing SpringBank database with sample data...");
        
        try {
            // Check if data already exists to prevent duplicates
            if (customerRepository.count() > 0) {
                logger.info("📊 Database already contains {} customers. Skipping initialization.", 
                           customerRepository.count());
                return;
            }
            
            createSampleCustomers();
            logger.info("✅ Database initialization completed successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Error during database initialization: ", e);
        }
    }
    
    private void createSampleCustomers() {
        // Create Sample Customers for Testing
        Customer customer1 = createCustomer("John Doe", "john.doe@example.com", "555-0101");
        Customer customer2 = createCustomer("Jane Smith", "jane.smith@example.com", "555-0102");
        Customer customer3 = createCustomer("Muhammad Danial", "danielsswork@gmail.com", "01169427674");
        Customer customer4 = createCustomer("Alice Johnson", "alice.johnson@example.com", "555-0104");
        Customer customer5 = createCustomer("Bob Wilson", "bob.wilson@example.com", "555-0105");
        
        logger.info("👥 Created {} sample customers", customerRepository.count());
        
        // Log created data for reference
        logCreatedData();
    }
    
    private Customer createCustomer(String name, String email, String phone) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setCreatedDate(LocalDateTime.now());
        
        return customerRepository.save(customer);
    }
    
    private void logCreatedData() {
        logger.info("\n" +
                   "📋 SAMPLE DATA CREATED FOR TESTING:\n" +
                   "=====================================\n" +
                   "👥 CUSTOMERS AVAILABLE FOR TESTING:\n" +
                   "  • ID 1: John Doe (john.doe@example.com)\n" +
                   "  • ID 2: Jane Smith (jane.smith@example.com)\n" +
                   "  • ID 3: Muhammad Danial (danielsswork@gmail.com)\n" +
                   "  • ID 4: Alice Johnson (alice.johnson@example.com)\n" +
                   "  • ID 5: Bob Wilson (bob.wilson@example.com)\n" +
                   "\n💡 TESTING TIPS:\n" +
                   "  • Use Customer IDs 1-5 for inquiry testing\n" +
                   "  • Check H2 Console: http://localhost:8080/h2-console\n" +
                   "  • Database URL: jdbc:h2:mem:springbank\n" +
                   "  • Username: sa, Password: password\n" +
                   "\n🌐 FRONTEND TESTING:\n" +
                   "  • GET http://localhost:8080/api/customers (should return 5 customers)\n" +
                   "  • GET http://localhost:8080/api/customers/1 (should return John Doe)\n" +
                   "  • React UI: http://localhost:3000 (inquiry should work immediately)\n" +
                   "=====================================");
    }
}