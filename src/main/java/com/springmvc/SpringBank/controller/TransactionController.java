@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    /**
     * DEPOSIT CASH API
     * Assessment Requirement: "Deposit Cash: Accepts account number and deposit amount, and updates the balance."
     * 
     * POST /api/transactions/deposit
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        logger.info("Deposit request: {} to account: {}", request.getAmount(), request.getAccountNumber());
        
        Transaction transaction = transactionService.deposit(
            request.getAccountNumber(),
            request.getAmount(),
            request.getDescription()
        );
        
        TransactionResponse response = TransactionResponse.from(transaction);
        
        logger.info("Deposit successful: Transaction ID {}", response.getTransactionId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * WITHDRAW CASH API  
     * Assessment Requirement: "Withdraw Cash: Accepts account number and withdrawal amount, and updates the balance."
     * 
     * POST /api/transactions/withdraw
     */
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        logger.info("Withdrawal request: {} from account: {}", request.getAmount(), request.getAccountNumber());
        
        Transaction transaction = transactionService.withdraw(
            request.getAccountNumber(),
            request.getAmount(),
            request.getDescription()
        );
        
        TransactionResponse response = TransactionResponse.from(transaction);
        
        logger.info("Withdrawal successful: Transaction ID {}", response.getTransactionId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET TRANSACTION HISTORY
     * Additional utility endpoint
     * 
     * GET /api/transactions/account/{accountNumber}
     */
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable String accountNumber) {
        logger.info("Transaction history request for account: {}", accountNumber);
        
        List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
        
        List<TransactionResponse> responses = transactions.stream()
            .map(TransactionResponse::from)
            .collect(Collectors.toList());
        
        logger.info("Found {} transactions for account: {}", responses.size(), accountNumber);
        return ResponseEntity.ok(responses);
    }
}