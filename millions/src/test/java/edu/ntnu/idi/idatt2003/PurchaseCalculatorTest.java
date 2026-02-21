package edu.ntnu.idi.idatt2003;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import  java.math.BigDecimal;



class PurchaseCalculatorTest {
  private Stock appleStock;
  private Share appleShare;
  private PurchaseCalculator calculator;
  @BeforeEach
void setUp() {
    appleStock=new Stock("AAPL", "Apple INC", new BigDecimal("150.00"));
    appleShare=new Share(appleStock, new BigDecimal("10"), new BigDecimal("100.00"));
    calculator = new PurchaseCalculator(appleShare);
  }
  @Test
  void calculatePurchase() {
    assertNotNull(calculator);
  }
  @Test
  void testConstructorWithNullShare() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new PurchaseCalculator(null));
    assertTrue(exception.getMessage().contains("Share"));

  }
  @Test
  void testCalculateGross() {
    BigDecimal gross =calculator.calculateGross();
    assertEquals(new BigDecimal("1000.00"),gross);
  }
  @Test
  void testCalculateCommission() {
    BigDecimal commision=calculator.calculateCommission();
    assertEquals(new BigDecimal("5.00"),commision);

  }
  @Test
  void testCalculateTax() {
    //No tax on purchases
    BigDecimal tax=calculator.calculateTax();
    assertEquals(new BigDecimal("0.00"),tax);
  }
  @Test
  void testCalculateTotal() {
    BigDecimal total=calculator.calculateTotal();
    assertEquals(new BigDecimal("1005.00"),total);
  }
}
