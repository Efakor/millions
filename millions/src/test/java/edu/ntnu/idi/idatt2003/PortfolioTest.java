package edu.ntnu.idi.idatt2003;

import org.junit.jupiter.api.BeforeEach;
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
        googleShare = new Share(googleStock, new BigDecimal("2"), new BigDecimal("2800.00"))
    }

    @Test
    void constructorCreatesEmptyPortfolio() {
        assertTrue(portfolio.isEmpty());
        assertEquals(0, portfolio.size());
    }


}