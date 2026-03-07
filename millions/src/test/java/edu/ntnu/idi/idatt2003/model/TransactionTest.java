package edu.ntnu.idi.idatt2003.model;

import edu.ntnu.idi.idatt2003.model.calculator.TransactionCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Transaction class.
 * Since Transaction is abstract, we test through a concrete mock implementation.
 */
class TransactionTest {

    /**
     * Mock implementation of Transaction for testing.
     */
    private static class MockTransaction extends Transaction {
        public MockTransaction(Share share, int week, TransactionCalculator calculator) {
            super(share, week, calculator);
        }

        @Override
        public void commit(Player player) {
            if (isCommitted()) {
                throw new IllegalStateException("Transaction already committed");
            }
            setCommitted(true);
        }
    }

    /**
     * Mock implementation of TransactionCalculator for testing.
     */
    private static class MockCalculator implements TransactionCalculator {
        private final BigDecimal gross;

        public MockCalculator(BigDecimal gross) {
            this.gross = gross;
        }

        @Override
        public BigDecimal calculateGross() {
            return gross;
        }

        @Override
        public BigDecimal calculateCommission() {
            return gross.multiply(new BigDecimal("0.005")); // 0.5%
        }

        @Override
        public BigDecimal calculateTax() {
            return BigDecimal.ZERO;
        }

        @Override
        public BigDecimal calculateTotal() {
            return calculateGross().add(calculateCommission());
        }
    }

    private Stock stock;
    private Share share;
    private TransactionCalculator calculator;
    private Player player;

    @BeforeEach
    void setUp() {
        stock = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
        share = new Share(stock, new BigDecimal("10"), new BigDecimal("150.00"));
        calculator = new MockCalculator(new BigDecimal("1500.00"));
        player = new Player("Test Player", new BigDecimal("10000.00"));
    }

    @Test
    void constructorWithValidParameters() {
        Transaction transaction = new MockTransaction(share, 1, calculator);

        assertNotNull(transaction);
        assertEquals(share, transaction.getShare());
        assertEquals(1, transaction.getWeek());
        assertFalse(transaction.isCommitted());
    }

    @Test
    void constructorWithNullShare() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MockTransaction(null, 1, calculator);
        });
    }

    @Test
    void constructorWithNullCalculator() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MockTransaction(share, 1, null);
        });
    }

    @Test
    void constructorWithZeroWeek() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MockTransaction(share, 0, calculator);
        });
    }

    @Test
    void constructorWithNegativeWeek() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MockTransaction(share, -1, calculator);
        });
    }

    @Test
    void getGrossValueReturnsCalculatorValue() {
        Transaction transaction = new MockTransaction(share, 1, calculator);

        assertEquals(new BigDecimal("1500.00"), transaction.getGrossValue());
    }

    @Test
    void getCommissionReturnsCalculatorValue() {
        Transaction transaction = new MockTransaction(share, 1, calculator);

        assertEquals(new BigDecimal("7.50000"), transaction.getCommission());
    }

    @Test
    void getTaxReturnsCalculatorValue() {
        Transaction transaction = new MockTransaction(share, 1, calculator);

        assertEquals(BigDecimal.ZERO, transaction.getTax());
    }

    @Test
    void getTotalValueReturnsCalculatorValue() {
        Transaction transaction = new MockTransaction(share, 1, calculator);

        assertEquals(new BigDecimal("1507.50000"), transaction.getTotalValue());
    }

    @Test
    void isCommitted_initiallyFalse() {
        Transaction transaction = new MockTransaction(share, 1, calculator);

        assertFalse(transaction.isCommitted());
    }

    @Test
    void commitSetsCommittedToTrue() {
        Transaction transaction = new MockTransaction(share, 1, calculator);

        transaction.commit(player);

        assertTrue(transaction.isCommitted());
    }

    @Test
    void commitWhenAlreadyCommitted() {
        Transaction transaction = new MockTransaction(share, 1, calculator);
        transaction.commit(player);

        assertThrows(IllegalStateException.class, () -> {
            transaction.commit(player);
        });
    }
}
