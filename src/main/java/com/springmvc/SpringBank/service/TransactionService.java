@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountService accountService;
    
    /**
     * REQUIREMENT 6: Deposit Cash
     * "Accepts account number and deposit amount, and updates the balance"
     */
    public Transaction deposit(String accountNumber, BigDecimal amount, String description) {
        logger.info("Processing deposit: {} to account: {}", amount, accountNumber);
        
        // Validate account exists and is active
        Account account = accountService.findByAccountNumber(accountNumber);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot deposit to non-active account: " + accountNumber);
        }
        
        // Calculate new balance
        BigDecimal newBalance = account.getBalance().add(amount);
        
        // Update account balance
        accountService.updateBalance(accountNumber, newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            accountNumber,
            TransactionType.DEPOSIT,
            amount,
            newBalance,
            description
        );
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        logger.info("Deposit completed: {} | New balance: {}", accountNumber, newBalance);
        return savedTransaction;
    }
    
    /**
     * REQUIREMENT 7: Withdraw Cash
     * "Accepts account number and withdrawal amount, and updates the balance"
     */
    public Transaction withdraw(String accountNumber, BigDecimal amount, String description) {
        logger.info("Processing withdrawal: {} from account: {}", amount, accountNumber);
        
        // Validate account exists and is active
        Account account = accountService.findByAccountNumber(accountNumber);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot withdraw from non-active account: " + accountNumber);
        }
        
        // Check sufficient funds
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                "Insufficient balance. Available: " + account.getBalance() + ", Requested: " + amount
            );
        }
        
        // Calculate new balance
        BigDecimal newBalance = account.getBalance().subtract(amount);
        
        // Update account balance
        accountService.updateBalance(accountNumber, newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            accountNumber,
            TransactionType.WITHDRAWAL,
            amount,
            newBalance,
            description
        );
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        logger.info("Withdrawal completed: {} | New balance: {}", accountNumber, newBalance);
        return savedTransaction;
    }
    
    /**
     * Get transaction history for an account
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(String accountNumber) {
        // Validate account exists
        accountService.findByAccountNumber(accountNumber);
        
        return transactionRepository.findByAccountNumberOrderByTransactionDateDesc(accountNumber);
    }
}