package edu.ntnu.idi.idatt2003.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Portfolio class.
 */
class PortfolioTest {
    private Portfolio portfolio;
    private Stock appleStock;
    private Stock googleStock;
    private Share appleShare1;
    private Share appleShare2;
    private Share googleShare;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();

        // Create stocks
        appleStock = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
        googleStock = new Stock("GOOGL", "Alphabet Inc.", new BigDecimal("2800.00"));

        // Create shares
        appleShare1 = new Share(appleStock, new BigDecimal("10"), new BigDecimal("150.00"));
        appleShare2 = new Share(appleStock, new BigDecimal("5"), new BigDecimal("135.00"));
        googleShare = new Share(googleStock, new BigDecimal("2"), new BigDecimal("2800.00"));
    }

    @Test
    void constructorCreatesEmptyPortfolio() {
        assertTrue(portfolio.isEmpty());
        assertEquals(0, portfolio.size());
    }

    @Test
    void addShareWithValidShare() {
        assertTrue(portfolio.addShare(appleShare1));
        assertEquals(1, portfolio.size());
        assertFalse(portfolio.isEmpty());
    }

    @Test
    void addShareWithNullShare() {
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.addShare(null);
        });
    }

    @Test
    void addShareMultipleShares() {
        portfolio.addShare(appleShare1);
        portfolio.addShare(appleShare2);
        portfolio.addShare(googleShare);

        assertEquals(3, portfolio.size());
    }

    @Test
    void removeShareWithExistingShare() {
        portfolio.addShare(appleShare1);
        portfolio.addShare(googleShare);

        assertTrue(portfolio.removeShare(appleShare1));
        assertEquals(1, portfolio.size());
        assertFalse(portfolio.contains(appleShare1));
    }

    @Test
    void removeShareWithNonExistingShare() {
        portfolio.addShare(appleShare1);

        assertFalse(portfolio.removeShare(googleShare));
        assertEquals(1, portfolio.size());
    }

    @Test
    void removeShareWithNullShare() {
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.removeShare(null);
        });
    }

    @Test
    void getSharesReturnsAllShares() {
        portfolio.addShare(appleShare1);
        portfolio.addShare(googleShare);

        List<Share> shares = portfolio.getAllShares();

        assertEquals(2, shares.size());
        assertTrue(shares.contains(appleShare1));
        assertTrue(shares.contains(googleShare));
    }

    @Test
    void getSharesReturnsUnmodifiableList() {
        portfolio.addShare(appleShare1);
        List<Share> shares = portfolio.getAllShares();

        assertThrows(UnsupportedOperationException.class, () -> {
            shares.add(googleShare);
        });
    }

    @Test
    void getSharesBySymbolWithValidSymbol() {
        portfolio.addShare(appleShare1);
        portfolio.addShare(appleShare2);
        portfolio.addShare(googleShare);

        List<Share> appleShares = portfolio.getShares("AAPL");

        assertEquals(2, appleShares.size());
        assertTrue(appleShares.contains(appleShare1));
        assertTrue(appleShares.contains(appleShare2));
        assertFalse(appleShares.contains(googleShare));
    }

    @Test
    void getSharesBySymbolWithNonExistingSymbol() {
        portfolio.addShare(appleShare1);

        List<Share> shares = portfolio.getShares("MSFT");

        assertTrue(shares.isEmpty());
    }

    @Test
    void getSharesBySymbolWithNullSymbol() {
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.getShares(null);
        });
    }

    @Test
    void getSharesBySymbolWithEmptySymbol() {
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.getShares("");
        });
    }

    @Test
    void containsWithExistingShare() {
        portfolio.addShare(appleShare1);

        assertTrue(portfolio.contains(appleShare1));
    }

    @Test
    void containsWithNonExistingShare() {
        portfolio.addShare(appleShare1);

        assertFalse(portfolio.contains(googleShare));
    }

    @Test
    void containsWithNullShare() {
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.contains(null);
        });
    }

    @Test
    void sizeReflectsNumberOfShares() {
        assertEquals(0, portfolio.size());

        portfolio.addShare(appleShare1);
        assertEquals(1, portfolio.size());

        portfolio.addShare(googleShare);
        assertEquals(2, portfolio.size());

        portfolio.removeShare(appleShare1);
        assertEquals(1, portfolio.size());
    }

    @Test
    void isEmptyReflectsPortfolioState() {
        assertTrue(portfolio.isEmpty());

        portfolio.addShare(appleShare1);
        assertFalse(portfolio.isEmpty());

        portfolio.removeShare(appleShare1);
        assertTrue(portfolio.isEmpty());
    }

    @Nested
    class NetWorthTests {

        @Test
        void getNetWorthWithEmptyPortfolio() {
            assertEquals(0, BigDecimal.ZERO.compareTo(portfolio.getNetWorth()));
        }

        @Test
        void geNetWorthWithSingleShare() {
            portfolio.addShare(appleShare1);
            assertTrue(portfolio.getNetWorth().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        void geNetWorthWithMultipleShares() {
            portfolio.addShare(appleShare1);
            BigDecimal singleShareWorth = portfolio.getNetWorth();
            portfolio.addShare(googleShare);
            BigDecimal multipleShareWorth = portfolio.getNetWorth();

            assertTrue(singleShareWorth.compareTo(multipleShareWorth) < 0);
        }
    }
}