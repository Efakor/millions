package edu.ntnu.idi.idatt2003.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SaleCalculator class.
 * Tests all the calculation methods for sale transactions.
 */

class SaleCalculatorTest {
  private SaleCalculator calculator;

  /**
   * Sets up the test fixtures  before each test.
   * Creates a stock, a share and calculator (SaleCalculator).
   */

  @BeforeEach
  void setUp() {
    Stock appleStock = new Stock("AAPL", "Apple INC", new BigDecimal("150.00"));
    Share appleShare = new Share(appleStock, new BigDecimal("10"), new BigDecimal("100.00"));
    calculator = new SaleCalculator(appleShare);
  }

  /**
   * Constructor with a valid input test.
   */
  @Nested
  class ConstructorValidTests {
    /**
     * Tests SaleCalculator with valid inputs.
     */

    @Test
    void testConstructorWithValidShare() {
      assertNotNull(calculator);
    }

  }

  /**
   * Tests for SalesCalculator construction validation.
   */
  @Nested
  class ConstructorValidationTests {

    /**
     * Tests the constructor with null share, which throws IllegalArgumentException.
     */
    @Test
    void testConstructorWithNullShare() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new SaleCalculator(null));
      assertTrue(exception.getMessage().contains("Share"));

    }

  }

  @Nested
  class CalculationTests {
    /**
     * Test the calculateGross return the correct gross value.
     */
    @Test
    void testCalculateGross() {
      BigDecimal gross = calculator.calculateGross();
      assertEquals(new BigDecimal("1500.00"), gross);
    }

    /**
     * Tests that calculate Commission returns correct commission (1% of gross).
     */
    @Test
    void testCalculateCommission() {
      BigDecimal commission = calculator.calculateCommission();
      assertEquals(new BigDecimal("15.00"), commission);
    }

    /**
     * Test that calculates Tax return the correct tax.
     */
    @Test
    void testCalculateTax() {
      // Arrange & Act
      BigDecimal tax = calculator.calculateTax();
      // Assert
      assertEquals(new BigDecimal("145.50"), tax);

    }

    /**
     * Tests that calculates the total and returns the correct total of sale.
     */
    @Test
    void testCalculateTotal() {
      BigDecimal total = calculator.calculateTotal();
      assertEquals(new BigDecimal("1339.50"), total);
    }
  }
}
