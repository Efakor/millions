package edu.ntnu.idi.idatt2003.model;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class StockTest {
  private Stock appleStock;
  private BigDecimal initialPrice;

  @BeforeEach
  void setUp() {
    initialPrice = new BigDecimal("150.50");
    appleStock = new Stock("AAPL", "Apple Inc.", initialPrice);

  }

  //Constructor Tests
  @Nested
  class ConstructorValid{
    @Test
    void testConstructorWithValidInputs() {
      //Arrange & Act
      Stock stock = new Stock("GOOGL", "Alphabet Inc.", new BigDecimal("2800.00"));
      //Assert
      assertEquals("GOOGL", stock.getSymbol());
      assertEquals("Alphabet Inc.", stock.getCompany());
      assertEquals(new BigDecimal("2800.00"), stock.getSalesPrice());
    }
    @Test
    void testConstructorWithNullSymbols() {
      //Arrange,Assert and Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Stock(null, "Apple Inc.", new BigDecimal("2800.00")));
      assertTrue(exception.getMessage().contains("Stock symbol"));
    }
    @Test
    void testConstructorWithEmptySymbols() {
      //Arrange,Assert  and Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Stock("", "Apple Inc.", new BigDecimal("2800.00")));
      assertTrue(exception.getMessage().contains("Stock symbol"));

    }
    @Test
    void testConstructorWithBlankSymbols() {
      //Arrange,Assert and  Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Stock("  ", "Apple Inc.", new BigDecimal("2800.00")));
      assertTrue(exception.getMessage().contains("Stock symbol"));


    }


  }

  /**
   * Test for Stock construction validation, company name errors.
   */
  @Nested
  class ConstructorCompanyNameValidationTests{
    @Test
    void testConstructorWithNullCompany() {
      //Arrange,Assert and  Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Stock(" AAPL ", null, new BigDecimal("150.50")));
      assertTrue(exception.getMessage().contains("Company name"));

    }
    @Test
    void testConstructorWithEmptyCompany() {
      //Arrange,Assert and  Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Stock(" AAPL ", "", new BigDecimal("150.50")));
      assertTrue(exception.getMessage().contains("Company name"));

    }

  }
  @Nested
  class ConstructorSalesPriceValidationTests{
    @Test
    void testConstructorWithNullSalesPrice() {
      //Arrange,Assert and  Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Stock(" AAPL ", "Apple Inc.", null));
      assertTrue(exception.getMessage().contains("Sales price"));

    }
    @Test
    void testConstructorWithZeroSalesPrice() {
      //Arrange,Assert and  Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Stock(" AAPL ", "Apple Inc.", BigDecimal.ZERO));
      assertTrue(exception.getMessage().contains("Sales"));

    }
    @Test
    void testConstructorWithNegativeSalesPrice() {
      //Arrange,Assert and  Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new Stock(" AAPL ", "Apple Inc.", new BigDecimal("-150.50")));
      assertTrue(exception.getMessage().contains("Sales"));


    }


  }
  @Nested
  class ConstructorGetterTests{

    @Test
    void testGetSymbol() {
      //Arrange and Act
      String symbol = appleStock.getSymbol();
      //Assert
      assertEquals("AAPL", symbol);
    }
    @Test
    void testGetCompany() {
      //Arrange and Act
      String company = appleStock.getCompany();
      //Assert
      assertEquals("Apple Inc.", appleStock.getCompany());
    }
    @Test
    void testGetSalesPriceWithReturnsInitialPrice() {
      //Arrange and Act
      BigDecimal salesPrice = appleStock.getSalesPrice();
      //Assert
      assertEquals(initialPrice, salesPrice);
    }

  }
  @Nested
  class AddTests{
    @Test
    void testAddNewSalesPriceWithValidPrice() {
      //Arrange
      BigDecimal newPrice = new BigDecimal("160.50");
      //Act
      appleStock.addNewSalesPrice(newPrice);
      //Assert
      assertEquals(newPrice, appleStock.getSalesPrice());


    }

    @Test
    void testaddNewSalesPriceWithZeroPrice() {
      //Arrange and Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> appleStock.addNewSalesPrice(BigDecimal.ZERO));
      //Assert
      assertTrue(exception.getMessage().contains("Sales"));
    }
    @Test
    void testaddNewSalesPriceWithNegativePrice() {
      //Arrange and Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> appleStock.addNewSalesPrice(new BigDecimal("-150.50")));

      //Assert
      assertTrue(exception.getMessage().contains("Sales"));

    }
    @Test
    void testAddMultiplePrices() {
      BigDecimal price2 = new BigDecimal("158.50");
      BigDecimal price3 = new BigDecimal("160.50");
      BigDecimal price4 = new BigDecimal("164.50");

      //Act
      appleStock.addNewSalesPrice(price2);
      appleStock.addNewSalesPrice(price3);
      appleStock.addNewSalesPrice(price4);

      //Assert
      assertEquals(price4, appleStock.getSalesPrice());
      assertEquals(4, appleStock.getPriceHistory().size());
    }
  }
  @Nested
  class GetPriceHistoryTests{
    //Price History tests
    @Test
    void testGetPriceHistoryInitialSize() {
      //Arrange
      var history = appleStock.getPriceHistory();
      //Assert
      assertEquals(1, history.size());
      assertEquals(initialPrice, history.get(0));
    }
    @Test
    void testGetPriceHistoryAfterAddingPrice() {
      //Arrange
      BigDecimal price2 = new BigDecimal("160.50");
      BigDecimal price3 = new BigDecimal("164.50");
      appleStock.addNewSalesPrice(price2);
      appleStock.addNewSalesPrice(price3);
      //Act
      var history = appleStock.getPriceHistory();
      //Assert
      assertEquals(3, history.size());
      assertEquals(initialPrice, history.get(0));
      assertEquals(price2, history.get(1));
      assertEquals(price3, history.get(2));
    }

    @Test
    void testGetPriceHistoryReturnsNewPrice() {
      //Arrange and Act
      var history1 = appleStock.getPriceHistory();
      var history2 = appleStock.getPriceHistory();
      //Assert
      assertNotSame(history1, history2);
    }
    @Test
    void testWithEqualSameSymbol() {
      //Arrange and Act
      Stock stock1= new Stock("AAPL", "Apple Inc.", new BigDecimal("150.50"));
      Stock stock2 = new Stock("AAPL", "Apple Inc.", new BigDecimal("155.50"));
      //Assert
      assertEquals(stock1, stock2);
    }
    @Test
    void testEqualsWithDifferentSymbol() {
      //Arrange and Act
      Stock stock1= new Stock("AAPL", "Apple Inc.", new BigDecimal("150.50"));
      Stock stock2= new Stock("GOOGL", "Alphabet Inc..", new BigDecimal("2800.00"));
      assertNotEquals(stock1, stock2);
    }

  }

}


