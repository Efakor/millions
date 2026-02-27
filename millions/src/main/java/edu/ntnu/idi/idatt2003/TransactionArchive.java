package edu.ntnu.idi.idatt2003;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Archives all completed transactions for a player.
 * Provides methods to retrieve, filter, and analyze transactions by week or type.
 *
 * @author efakor
 * @version 1.0
 */
public class TransactionArchive {
    private final List<Transaction> transactions;

    /**
     * Constructs a new empty TransactionArchive.
     */
    public TransactionArchive() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds a transaction to the archive.
     * Normally called when a transaction is committed.
     *
     * @param transaction the transaction to add
     * @return true if the transaction was added successfully
     * @throws IllegalArgumentException if transaction is null
     */
    public boolean add(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        return transactions.add(transaction);
    }

    /**
     * Checks if the archive is empty.
     *
     * @return true if there are no transactions, false otherwise
     */
    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    /**
     * Gets all transactions from a specific week.
     * Includes both purchases and sales.
     *
     * @param week the week number
     * @return list of transactions from that week (empty if none)
     * @throws IllegalArgumentException if week is less than 1
     */
    public List<Transaction> getTransactions(int week) {
        if (week < 1) {
            throw new IllegalArgumentException("Week cannot be less than 1");
        }

        return transactions.stream()
                .filter(transaction -> transaction.getWeek() == week)
                .collect(Collectors.toList());
    }

    /**
     * Gets all transactions in the archive.
     *
     * @return unmodifiable list of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return List.copyOf(transactions);
    }

<<<<<<< HEAD
    /**
     * Counts the number of distinct weeks with actual trading.
     * A week is counted if it has at least one transaction.
     * Useful for calculating average performance per active week.
     *
     * @return the number of weeks with transactions
     */
=======
    public List<Purchase> getPurchases(int week) {
        if (week < 1) {
            throw new IllegalArgumentException("Week must be positive");
        }

        return transactions.stream()
                .filter(t -> t.getWeek() == week)
                .filter(t -> t instanceof Purchase)
                .map(t -> (Purchase) t)
                .collect(Collectors.toList());
    }

    public List<Sale> getSales(int week) {
        if (week < 1) {
            throw new IllegalArgumentException("Week must be positive");
        }

        return transactions.stream()
                .filter(t -> t.getWeek() == week)
                .filter(t -> t instanceof Sale)
                .map(t -> (Sale) t)
                .collect(Collectors.toList());
    }

>>>>>>> 9e1ab9f6a82af1cc89f3873786075e685ff80e8b
    public int countDistinctWeeks() {
        return (int) transactions.stream()
                .map(Transaction::getWeek)
                .distinct()
                .count();
    }
}