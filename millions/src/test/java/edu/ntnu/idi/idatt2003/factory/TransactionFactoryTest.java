package edu.ntnu.idi.idatt2003.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.idatt2003.model.Purchase;
import edu.ntnu.idi.idatt2003.model.Sale;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * TransactionFactory tests.
 */
public class TransactionFactoryTest {
  private Share share;

  @BeforeEach
  void setUp() {
    Stock stock = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
    share = new Share(stock, new BigDecimal("10"), new BigDecimal("150.00"));

  }

  @Nested
  @DisplayName("Valid Tests")
  class CreateTransaction {
    @Test
    void createPurchase_returnsNotNull() {
      Purchase result = TransactionFactory.createPurchase(share, 1);
      assertNotNull(result);

    }

    @Test
    void createPurchase_returnsCorrectType() {
      Purchase result = TransactionFactory.createPurchase(share, 1);
      assertInstanceOf(Purchase.class, result);
    }

    @Test
    void createSale_returnNotNull() {
      Sale result = TransactionFactory.createSale(share, 1);
      assertNotNull(result);
    }

    @Test
    void createSale_returnCorrectType() {
      Sale result = TransactionFactory.createSale(share, 1);
      assertInstanceOf(Sale.class, result);
    }

    @Test
    void createPurchase_returnCorrectWeek() {
      Purchase result = TransactionFactory.createPurchase(share, 5);
      assertEquals(5, result.getWeek());
    }

    @Test
    void createSale_returnCorrectWeek() {
      Sale result = TransactionFactory.createSale(share, 5);
      assertEquals(5, result.getWeek());
    }
  }

  @Test
  void createPurchase_returnCorrectShare() {
    Purchase result = TransactionFactory.createPurchase(share, 1);
    assertEquals(share, result.getShare());
  }

  @Test
  void createSale_returnCorrectShare() {
    Sale result = TransactionFactory.createSale(share, 1);
    assertEquals(share, result.getShare());
  }

  @DisplayName("Invalid tests")
  @Nested
  class InvalidTests {
    @Test
    void createSale_nullShare() {
      assertThrows(IllegalArgumentException.class, () -> TransactionFactory.createSale(null, 1));
    }

    @Test
    void createPurchase_nullWeek() {
      assertThrows(IllegalArgumentException.class, () ->
          TransactionFactory.createPurchase(null, 1));
    }

    @Test
    void createSale_negativeWeek() {
      assertThrows(IllegalArgumentException.class, () -> TransactionFactory.createSale(share, -1));
    }

    @Test
    void createPurchase_negativeWeek() {
      assertThrows(IllegalArgumentException.class, () ->
          TransactionFactory.createPurchase(share, -1));
    }

  }
}


