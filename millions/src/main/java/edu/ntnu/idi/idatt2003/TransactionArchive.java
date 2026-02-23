package edu.ntnu.idi.idatt2003;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Archives all completed transactions for a player.
 * Provides methods to retrieve transactions by week or type.
 */
public class TransactionArchive {
    private final List<Transaction> transactions;

    public TransactionArchive() {
        this.transactions = new ArrayList<>();
    }

    public boolean add(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        return transactions.add(transaction);
    }

    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    public List<Transaction> getTransactions(int week) {
        if (week < 1) {
            throw new IllegalArgumentException("Week cannot be less than 1");
        }

        return transactions.stream()
                .filter(transaction -> transaction.getWeek() == week)
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactions() {
        return List.copyOf(transactions);
    }

    public int countDistinctWeeks() {
        return (int) transactions.stream()
                .map(Transaction::getWeek)
                .distinct()
                .count();
    }
}