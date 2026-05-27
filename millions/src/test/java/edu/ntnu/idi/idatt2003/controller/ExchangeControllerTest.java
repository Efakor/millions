package edu.ntnu.idi.idatt2003.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Player;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ExchangeController.
 * Tests buying,selling,advancing weeks and searching for stocks.
 * Follows A-A-A (Arrange-Act-Assert) pattern.
 */

class ExchangeControllerTest {
  private ExchangeController exchangeController;
  private Exchange exchange;
  private Player player;

  /**
   * Sets up fresh exchange,player,controller before each test.
   * Adds one stock(AAPL) to the exchange with a price of 150.
   */

  @BeforeEach
  void setUp() {
    PlayerController playerController = new PlayerController();
    playerController.newGame("Kari", new BigDecimal("10000"));
    playerController.getExchange().addStock(new Stock("AAPL", "Apple", new BigDecimal("150")));
    exchangeController = new ExchangeController(playerController.getExchange(), playerController);
    player = playerController.getPlayer();
    exchange = playerController.getExchange();
  }

  /**
   * Tests for buying.
   */
  @Nested
  class BuyTests {
    /**
     * Test that buying a valid  stock adds a stock to the player's portfolio.
     */
    @Test
    void buyValidPlayerShare() {
      // Arrange
      BigDecimal quantity = new BigDecimal("2");
      // Act
      exchangeController.buy("AAPL", quantity);
      // Assert
      assertFalse(player.getPortfolio().getAllShares().isEmpty());
    }

    /**
     * Test that buying a stock deducts the correct amount form the players balance.
     */
    @Test
    void buyValidMoneyDeduction() {
      // Arrange
      BigDecimal before = player.getCurrentMoney();
      BigDecimal quantity = new BigDecimal("2");
      // Act
      exchangeController.buy("AAPL", quantity);
      // Assert
      assertTrue(player.getCurrentMoney().compareTo(before) < 0);
    }

    /**
     * Test that buy a stock, but has insufficient funds, and throws IllegalStateException.
     */
    @Test
    void buyInsufficientFunds_throwsException() {
      // Arrange
      BigDecimal quantity = new BigDecimal("99999");
      // Assert
      assertThrows(IllegalStateException.class, () ->
          exchangeController.buy("AAPL", quantity));
    }

    /**
     * Test that trying to buy a stock with Invalid symbol, throws IllegalArgumentException.
     */
    @Test
    void buyInvalidSymbol_throwsException() {
      // Assert
      assertThrows(IllegalArgumentException.class, () ->
          exchangeController.buy("Invalid", new BigDecimal("99999")));
    }
  }

  /**
   * Tests for the sell operation.
   */
  @Nested
  class SellTests {
    /**
     * Test selling a share and removes from the player's portfolio.
     */
    @Test
    void sellValidSymbol_shareRemovedPortfolio() {
      // Arrange
      exchangeController.buy("AAPL", new BigDecimal("2"));
      Share share = player.getPortfolio().getAllShares().getFirst();
      // Act
      exchangeController.sell(share);
      // Assert
      assertTrue(player.getPortfolio().getAllShares().isEmpty());

    }

    /**
     * Tests that selling a share increases the player's cash balance.
     */
    @Test
    void sellValid_moneyIncreasedAfterSell() {
      // Arrange
      exchangeController.buy("AAPL", BigDecimal.ONE);
      BigDecimal moneyAfter = player.getCurrentMoney();
      Share share = player.getPortfolio().getAllShares().getFirst();
      // Act
      exchangeController.sell(share);
      // Assert
      assertTrue(player.getCurrentMoney().compareTo(moneyAfter) > 0);
    }
  }

  /**
   * Tests the stock search functionality.
   */
  @Nested
  class SearchTests {
    /**
     * Tests that searching by company name and return matching stock.
     */
    @Test
    void searchForStock() {
      // Arrange
      String search = "Apple";
      List<Stock> stocks = exchangeController.findStocks(search);
      assertFalse(stocks.isEmpty());
      assertEquals("AAPL", stocks.getFirst().getSymbol());
    }

    /**
     * Tests that searching a non-existent stock and returns an empty list.
     */
    @Test
    void searchStock_noMatch() {
      // Arrange
      String search = "RRRRR";
      // Act
      List<Stock> stocks = exchangeController.findStocks(search);
      // Assert
      assertTrue(stocks.isEmpty());
    }
  }

  /**
   * Tests for the week advancement functionality.
   */
  @Nested
  class AdvanceTests {
    /**
     * Tests that advancing the week increments the week counter by one.
     */
    @Test
    void advanceWeekIncrements() {
      // Arrange
      int weekBefore = exchange.getWeek();
      // Act
      exchange.advance();
      // Assert
      assertEquals(weekBefore + 1, exchange.getWeek());
    }

    /**
     * Tests for advancing week add a new price to the stock's price history.
     */
    @Test
    void advanceWeek_priceHistoryGrows() {
      // Arrange
      int historySizeBefore = exchange.getStock("AAPL").getHistoricalPrices().size();
      // Act
      exchange.advance();
      // Assert
      assertTrue(exchange.getStock("AAPL").getHistoricalPrices().size() > historySizeBefore);
    }
  }

}


