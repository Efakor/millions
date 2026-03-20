package edu.ntnu.idi.idatt2003.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the Share class.
 * Tests both positive scenarios and negative cases
 */
class ShareTest {
  private Stock appleStock;
  private Share appleShare;
  private BigDecimal purchasePrice;
  private BigDecimal quantity;

  @BeforeEach
  void setUp() {
    appleStock = new Stock("AAPL", "Apple Inc", new BigDecimal("150.00"));
    quantity = new BigDecimal("10");
    purchasePrice = new BigDecimal("145.50");
    appleShare = new Share(appleStock, quantity, purchasePrice);
  }

  // Constructor Tests
  @Nested
  class ConstructorValid {
    @Test
    void testConstructorWithValidInputs() {
      // Arrange & Act
      Share share = new Share(appleStock, new BigDecimal("6"), new BigDecimal("130.00"));
      // Assert
      assertNotNull(share);
      assertEquals(appleStock, share.stock());
      assertEquals(new BigDecimal("6"), share.quantity());
      assertEquals(new BigDecimal("130.00"), share.purchasePrice());

    }

  }

  @Nested
  class ValidateStocks {
    @Test
    void testConstructorWithNullStocks() {
      // Arrange,Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Share(null, new BigDecimal("10"),
              new BigDecimal("150.00")));
      assertTrue(exception.getMessage().contains("Stock"));
    }
  }

  @Nested
  class ConstructorQuantityValidationTests {
    @Test
    void testConstructorWithNullQuantity() {
      // Arrange,Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Share(appleStock, null, new BigDecimal("100.00"))
      );
      assertTrue(exception.getMessage().contains("Quantity"));
    }

    @Test
    void testConstructorWithZeroQuantity() {
      // Arrange,Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Share(appleStock, BigDecimal.ZERO, new BigDecimal("100.00")));
      assertTrue(exception.getMessage().contains("Quantity"));

    }

    @Test
    void testConstructorWithNegativeQuantity() {
      // Arrange,Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Share(appleStock, new BigDecimal("-5"), new BigDecimal("100.00")));
      assertTrue(exception.getMessage().contains("Quantity"));

    }

  }

  @Nested
  class ConstructorPurchasePriceValidationTests {
    @Test
    void testConstructorWithZeroPurchasePrice() {
      // Arrange,Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Share(appleStock, new BigDecimal("10"), BigDecimal.ZERO));
      assertTrue(exception.getMessage().contains("Purchase"));

    }

    @Test
    void testConstructorWithNullPurchasePrice() {
      // Arrange,Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Share(appleStock, new BigDecimal("10"), null));
      assertTrue(exception.getMessage().contains("Purchase"));
    }

    @Test
    void testConstructorWithNegativePurchasePrice() {
      // Arrange,Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Share(appleStock, new BigDecimal("10"), new BigDecimal("-50.00")));
      assertTrue(exception.getMessage().contains("Purchase"));

    }

  }

  @Nested
  class GetterTests {
    // Getter Tests
    @Test
    void testGetStock() {
      Stock stock = appleShare.stock();
      assertEquals(appleStock, stock);
      assertEquals("AAPL", stock.getSymbol());
    }

    @Test
    void testGetQuantity() {
      // Arrange & Act
      BigDecimal result = appleShare.quantity();
      // Assert
      assertEquals(quantity, result);
      assertEquals(new BigDecimal("10"), result);
    }

    @Test
    void testGetPurchasePrice() {
      BigDecimal result = appleShare.purchasePrice();
      assertEquals(purchasePrice, result);
      assertEquals(new BigDecimal("145.50"), result);
    }
  }
}
