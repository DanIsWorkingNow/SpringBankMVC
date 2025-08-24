@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Find transactions by account number
    List<Transaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber);
    
    // Find transactions by type
    List<Transaction> findByTransactionType(TransactionType transactionType);
    
    // Find recent transactions (last N transactions)
    @Query("SELECT t FROM Transaction t WHERE t.accountNumber = :accountNumber ORDER BY t.transactionDate DESC LIMIT :limit")
    List<Transaction> findRecentTransactions(@Param("accountNumber") String accountNumber, @Param("limit") int limit);
}