package edu.ntnu.idi.idatt2003;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TransactionArchive class.
 */
class TransactionArchiveTest {

    /**
     * Mock Transaction for testing.
     */
    private static class MockTransaction extends Transaction {
        public MockTransaction(Share share, int week, TransactionCalculator calculator) {
            super(share, week, calculator);
        }

        @Override
        public void commit(Player player) {
            setCommitted(true);
        }
    }

    /**
     * Mock Calculator for testing.
     */
    private static class MockCalculator implements TransactionCalculator {
        @Override
        public BigDecimal calculateGross() { return new BigDecimal("1000"); }

        @Override
        public BigDecimal calculateCommission() { return new BigDecimal("5"); }

        @Override
        public BigDecimal calculateTax() { return BigDecimal.ZERO; }

        @Override
        public BigDecimal calculateTotal() { return new BigDecimal("1005"); }
    }

    private TransactionArchive archive;
    private Stock stock;
    private Share share1;
    private Share share2;
    private TransactionCalculator calculator;

    @BeforeEach
    void setUp() {
        archive = new TransactionArchive();
        stock = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
        share1 = new Share(stock, new BigDecimal("10"), new BigDecimal("150.00"));
        share2 = new Share(stock, new BigDecimal("5"), new BigDecimal("160.00"));
        calculator = new MockCalculator();
    }

    @Test
    void constructorCreatesEmptyArchive() {
        assertTrue(archive.isEmpty());
    }

    @Test
    void addWithValidTransaction() {
        Transaction transaction = new MockTransaction(share1, 1, calculator);

        assertTrue(archive.add(transaction));
        assertFalse(archive.isEmpty());
    }

    @Test
    void addWithNullTransaction() {
        assertThrows(IllegalArgumentException.class, () -> {
            archive.add(null);
        });
    }

    @Test
    void isEmptyReflectsArchiveState() {
        assertTrue(archive.isEmpty());

        archive.add(new MockTransaction(share1, 1, calculator));
        assertFalse(archive.isEmpty());
    }

    @Test
    void getTransactionsWithSpecificWeek() {
        Transaction transaction1 = new MockTransaction(share1, 1, calculator);
        Transaction transaction2 = new MockTransaction(share2, 2, calculator);
        Transaction transaction3 = new MockTransaction(share1, 1, calculator);

        archive.add(transaction1);
        archive.add(transaction2);
        archive.add(transaction3);

        List<Transaction> week1Transactions = archive.getTransactions(1);

        assertEquals(2, week1Transactions.size());
        assertTrue(week1Transactions.contains(transaction1));
        assertTrue(week1Transactions.contains(transaction3));
    }

    @Test
    void getTransactionsWithNoMatchingWeek() {
        archive.add(new MockTransaction(share1, 1, calculator));

        List<Transaction> transactions = archive.getTransactions(5);

        assertTrue(transactions.isEmpty());
    }

    @Test
    void getTransactionsWithInvalidWeek() {
        assertThrows(IllegalArgumentException.class, () -> {
            archive.getTransactions(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            archive.getTransactions(-1);
        });
    }

    @Test
    void getAllTransactionsReturnsAllTransactions() {
        Transaction transaction1 = new MockTransaction(share1, 1, calculator);
        Transaction transaction2 = new MockTransaction(share2, 2, calculator);

        archive.add(transaction1);
        archive.add(transaction2);

        List<Transaction> allTransactions = archive.getAllTransactions();

        assertEquals(2, allTransactions.size());
        assertTrue(allTransactions.contains(transaction1));
        assertTrue(allTransactions.contains(transaction2));
    }

    @Test
    void getAllTransactionsReturnsUnmodifiableList() {
        archive.add(new MockTransaction(share1, 1, calculator));

        List<Transaction> transactions = archive.getAllTransactions();

        assertThrows(UnsupportedOperationException.class, () -> {
            transactions.add(new MockTransaction(share2, 2, calculator));
        });
    }

    @Test
    void countDistinctWeeksWithMultipleWeeks() {
        archive.add(new MockTransaction(share1, 1, calculator));
        archive.add(new MockTransaction(share2, 1, calculator));
        archive.add(new MockTransaction(share1, 2, calculator));
        archive.add(new MockTransaction(share2, 3, calculator));

        assertEquals(3, archive.countDistinctWeeks());
    }

    @Test
    void countDistinctWeeksWithEmptyArchive() {
        assertEquals(0, archive.countDistinctWeeks());
    }

    @Test
    void countDistinctWeeksWithSingleWeek() {
        archive.add(new MockTransaction(share1, 1, calculator));
        archive.add(new MockTransaction(share2, 1, calculator));

        assertEquals(1, archive.countDistinctWeeks());
    }
}