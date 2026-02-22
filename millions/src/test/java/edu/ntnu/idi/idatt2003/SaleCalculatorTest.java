package edu.ntnu.idi.idatt2003;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class SaleCalculatorTest {
  private SaleCalculator calculator;
  private Stock appleStock;
  private Share appleShare;

  @BeforeEach
  void setUp() {
    appleStock = new Stock("AAPL", "Apple INC", new BigDecimal("150.00"));
    appleShare = new Share(appleStock, new BigDecimal("10"), new BigDecimal("100.00"));
    calculator = new SaleCalculator(appleShare);
  }

  @Test
  void testConstructorWithValidShare() {
    assertNotNull(calculator);
  }
  @Test
  void testConstructorWithNullShare() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new SaleCalculator(null));
    assertTrue(exception.getMessage().contains("Share"));

  }
  @Test
  void testCalculateGross() {
    BigDecimal gross=calculator.calculateGross();
    assertEquals(new BigDecimal("1500.00"),gross);
  }
  @Test
  void testCalculateCommission() {
    BigDecimal commission=calculator.calculateCommission();
    assertEquals(new BigDecimal("15.00"),commission);
  }
  @Test
  void testCalculateTax() {
    //Arrange & Act
    BigDecimal tax=calculator.calculateTax();
    //Assert
    assertEquals(new BigDecimal("145.50"),tax);

  }
  @Test
  void testCalculateTotal() {
    BigDecimal total=calculator.calculateTotal();
    assertEquals(new BigDecimal("1339.50"),total);
  }

}
