package edu.ntnu.idi.idatt2003.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Player class.
 */
class PlayerTest {
    private Player player;
    private BigDecimal startingMoney;

    @BeforeEach
    void setUp() {
        startingMoney = new BigDecimal("10000.00");
        player = new Player("Test Player", startingMoney);
    }

    @Test
    void constructorWithValidParameters() {
        assertNotNull(player);
        assertEquals("Test Player", player.getName());
        assertEquals(startingMoney, player.getCurrentMoney());
        assertEquals(startingMoney, player.getStartingMoney());
        assertNotNull(player.getPortfolio());
        assertNotNull(player.getTransactionArchive());
    }

    @Test
    void constructorWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(null, startingMoney);
        });
    }

    @Test
    void constructorWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("", startingMoney);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Player("   ", startingMoney);
        });
    }

    @Test
    void constructorWithNullStartingMoney() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("Player", null);
        });
    }

    @Test
    void constructorWithZeroStartingMoney() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("Player", BigDecimal.ZERO);
        });
    }

    @Test
    void constructorWithNegativeStartingMoney() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("Player", new BigDecimal("-1000"));
        });
    }

    @Test
    void getNameReturnsCorrectName() {
        assertEquals("Test Player", player.getName());
    }

    @Test
    void getCurrentMoneyReturnsCurrentBalance() {
        assertEquals(startingMoney, player.getCurrentMoney());
    }

    @Test
    void getStartingMoneyReturnsOriginalAmount() {
        assertEquals(startingMoney, player.getStartingMoney());

        player.addMoney(new BigDecimal("1000"));
        assertEquals(startingMoney, player.getStartingMoney()); // Unchanged
    }

    @Test
    void getPortfolioReturnsPortfolio() {
        Portfolio portfolio = player.getPortfolio();
        assertNotNull(portfolio);
        assertTrue(portfolio.isEmpty());
    }

    @Test
    void getTransactionArchiveReturnsArchive() {
        TransactionArchive transactionArchive = player.getTransactionArchive();
        assertNotNull(transactionArchive);
        assertTrue(transactionArchive.isEmpty());
    }

    @Test
    void addMoneyWithValidAmount() {
        BigDecimal amountToAdd = new BigDecimal("1000.00");
        player.addMoney(amountToAdd);

        BigDecimal expected = startingMoney.add(amountToAdd);
        assertEquals(expected, player.getCurrentMoney());
    }

    @Test
    void addMoneyWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.addMoney(null);
        });
    }

    @Test
    void addMoneyWithZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.addMoney(BigDecimal.ZERO);
        });
    }

    @Test
    void addMoneyWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.addMoney(new BigDecimal("-100"));
        });
    }

    @Test
    void withdrawMoneyWithValidAmount() {
        BigDecimal amountToWithdraw = new BigDecimal("1000.00");
        player.withdrawMoney(amountToWithdraw);

        BigDecimal expected = startingMoney.subtract(amountToWithdraw);
        assertEquals(expected, player.getCurrentMoney());
    }

    @Test
    void withdrawMoneyWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.withdrawMoney(null);
        });
    }

    @Test
    void withdrawMoneyWithZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.withdrawMoney(BigDecimal.ZERO);
        });
    }

    @Test
    void withdrawMoneyWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.withdrawMoney(new BigDecimal("-100"));
        });
    }

    @Test
    void withdrawMoneyWithInsufficientFunds() {
        BigDecimal tooMuch = startingMoney.add(new BigDecimal("1000"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            player.withdrawMoney(tooMuch);
        });

        assertTrue(exception.getMessage().contains("Insufficient funds"));
    }

    @Test
    void withdrawMoneyWithExactBalanceSucceeds() {
        player.withdrawMoney(startingMoney);
        assertEquals(0, player.getCurrentMoney().compareTo(BigDecimal.ZERO));
    }

    @Test
    void multipleTransactionsUpdateBalanceCorrectly() {
        player.addMoney(new BigDecimal("500"));
        player.withdrawMoney(new BigDecimal("200"));
        player.addMoney(new BigDecimal("300"));

        BigDecimal expected = startingMoney
                .add(new BigDecimal("500"))
                .subtract(new BigDecimal("200"))
                .add(new BigDecimal("300"));

        assertEquals(expected, player.getCurrentMoney());
    }

    @Nested
    class NetWorthTests {

        @Test
        void getNetWorthWithNoSharesReturnsMoneyOnly() {
            Player player = new Player("Efa", new BigDecimal("1000.00"));
            assertEquals(0, new  BigDecimal("1000.00").compareTo(player.getNetWorth()));
        }

        @Test
        void getNetWorthWithSharesInPortfolio() {
            Player player = new Player("Efa", new BigDecimal("1000.00"));
            Stock stock = new Stock("AAPL", "Apple Inc.", new BigDecimal("100.00"));
            Share share = new Share(stock, new BigDecimal("2"), new BigDecimal("90.00"));
            player.getPortfolio().addShare(share);

            assertTrue(player.getNetWorth().compareTo(new BigDecimal("1000.00")) > 0);
        }
    }
}