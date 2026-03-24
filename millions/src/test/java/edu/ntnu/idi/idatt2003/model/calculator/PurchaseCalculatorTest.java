package edu.ntnu.idi.idatt2003.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import  java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for PurchaseCalculator  class.
 * Tests all calculation methods for purchase transactions.
 */

class PurchaseCalculatorTest {
  private PurchaseCalculator calculator;

  /**
   * Sets up test fixtures before each test.
   * Creates a stock, a share, a purchase calculator for testing.
   */
  @BeforeEach
void setUp() {
    Stock appleStock = new Stock("AAPL", "Apple INC", new BigDecimal("150.00"));
    Share appleShare = new Share(appleStock, new BigDecimal("10"), new BigDecimal("100.00"));
    calculator = new PurchaseCalculator(appleShare);
  }

  /**
   * Test constructors with valid inputs.
   */

  @Nested
  class ConstructorValidTests {
    /**
     * Tests PurchaseCalculator can be created with a valid share.
     */
    @Test
    void testConstructorWithValidInput() {
      assertNotNull(calculator);
    }
  }

  /**
   * Tests for constructor for validation.
   */
  @Nested
  class ConstructorValidationTests {
    /**
     * Tests PurchaseCalculator constructor when share is null,
     * throws IllegalArgumentException.
     *
     */
    @Test
    void testConstructorWithNullShare() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new PurchaseCalculator(null));
      assertTrue(exception.getMessage().contains("Share"));
    }
  }

  @Nested
  class CalculationTests {
    /**
     * Tests calculate gross returns the correct gross value.
     */
    @Test
    void testCalculateGross() {
      BigDecimal gross = calculator.calculateGross();
      assertEquals(new BigDecimal("1000.00"), gross);
    }

    /**
     * Tests calculate the commission and returns the correct commission (0.5% gross).
     */
    @Test
    void testCalculateCommission() {
      BigDecimal commission = calculator.calculateCommission();
      assertEquals(new BigDecimal("5.00"), commission);

    }

    /**
     * Tests calculate tax. There is no tax in purchasing shares.
     */
    @Test
    void testCalculateTax() {
      // No tax on purchases
      BigDecimal tax = calculator.calculateTax();
      assertEquals(new BigDecimal("0.00"), tax);
    }

    /**
     * Tests calculate total cost,returns the correct total cost.
     */
    @Test
    void testCalculateTotal() {
      BigDecimal total = calculator.calculateTotal();
      assertEquals(new BigDecimal("1005.00"), total);
    }
  }
}






