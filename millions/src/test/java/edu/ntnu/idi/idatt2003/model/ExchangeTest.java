package edu.ntnu.idi.idatt2003.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Exchange class.
 */
class ExchangeTest {
  private Exchange exchange;
  private Stock appleStock;
  private Stock googleStock;
  private Stock wellsFargoStock;
  private Player player;
  private List<Stock> stockList;

  @BeforeEach
  void setUp() {
    appleStock = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
    googleStock = new Stock("GOOGL", "Alphabet Inc.", new BigDecimal("2800.00"));
    wellsFargoStock = new Stock("WFC", "Wells Fargo", new BigDecimal("45.00"));

    stockList = new ArrayList<>();
    stockList.add(appleStock);
    stockList.add(googleStock);
    stockList.add(wellsFargoStock);

    exchange = new Exchange("NYSE", stockList);
    player = new Player("Test Player", new BigDecimal("100000.00"));
  }

  @Test
  void constructorWithValidNameAndStocks() {
    assertNotNull(exchange);
    assertEquals("NYSE", exchange.getName());
    assertEquals(1, exchange.getWeek());
    assertTrue(exchange.hasStock("AAPL"));
    assertTrue(exchange.hasStock("GOOGL"));
    assertTrue(exchange.hasStock("WFC"));
  }

  @Test
  void constructorWithNullName() {
    assertThrows(IllegalArgumentException.class, () -> new Exchange(null, stockList));
  }

  @Test
  void constructorWithEmptyName() {
    assertThrows(IllegalArgumentException.class, () -> new Exchange("", stockList));
  }

  @Test
  void constructorWithNullStockListCreatesEmptyExchange() {
    Exchange emptyExchange = new Exchange("NASDAQ", null);

    assertNotNull(emptyExchange);
    assertEquals("NASDAQ", emptyExchange.getName());
    assertFalse(emptyExchange.hasStock("AAPL"));
  }

  @Test
  void constructorWithEmptyStockListCreatesEmptyExchange() {
    Exchange emptyExchange = new Exchange("NASDAQ", new ArrayList<>());

    assertNotNull(emptyExchange);
    assertEquals("NASDAQ", emptyExchange.getName());
  }

  @Test
  void getNameReturnsCorrectName() {
    assertEquals("NYSE", exchange.getName());
  }

  @Test
  void getWeekInitiallyReturnsOne() {
    assertEquals(1, exchange.getWeek());
  }

  @Test
  void hasStockWithValidSymbol() {
    assertTrue(exchange.hasStock("AAPL"));
    assertTrue(exchange.hasStock("GOOGL"));
    assertTrue(exchange.hasStock("WFC"));
  }

  @Test
  void hasStockWithInvalidSymbol() {
    assertFalse(exchange.hasStock("MSFT"));
    assertFalse(exchange.hasStock("TSLA"));
  }

  @Test
  void hasStockWithNullSymbol() {
    assertThrows(IllegalArgumentException.class, () -> exchange.hasStock(null));
  }

  @Test
  void hasStockWithEmptySymbol() {
    assertThrows(IllegalArgumentException.class, () -> exchange.hasStock(""));
  }

  @Test
  void getStockWithValidSymbol() {
    Stock retrieved = exchange.getStock("AAPL");

    assertNotNull(retrieved);
    assertEquals(appleStock, retrieved);
    assertEquals("Apple Inc.", retrieved.getCompany());
  }

  @Test
  void getStockWithInvalidSymbol() {
    assertNull(exchange.getStock("MSFT"));
  }

  @Test
  void getStockWithNullSymbol() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getStock(null));
  }

  @Test
  void getAllStocksReturnsAllAddedStocks() {
    List<Stock> allStocks = exchange.getAllStocks();

    assertEquals(3, allStocks.size());
    assertTrue(allStocks.contains(appleStock));
    assertTrue(allStocks.contains(googleStock));
    assertTrue(allStocks.contains(wellsFargoStock));
  }

  @Test
  void getAllStocksReturnsUnmodifiableList() {
    List<Stock> stocks = exchange.getAllStocks();

    assertThrows(UnsupportedOperationException.class, () -> stocks.add(
        new Stock("MSFT", "Microsoft", new BigDecimal("300"))));
  }

  @Test
  void findStocksWithMatchingSymbol() {
    List<Stock> results = exchange.findStocks("GOOGL");

    assertEquals(1, results.size());
    assertEquals(googleStock, results.getFirst());
  }

  @Test
  void findStocksWithMatchingCompanyName() {
    List<Stock> results = exchange.findStocks("Apple");

    assertEquals(1, results.size());
    assertEquals(appleStock, results.getFirst());
  }

  @Test
  void findStocksWithPartialMatchReturnsMatches() {
    List<Stock> results = exchange.findStocks("App");

    assertEquals(1, results.size());
    assertEquals(appleStock, results.getFirst());
  }

  @Test
  void findStocksIsCaseInsensitive() {
    List<Stock> results = exchange.findStocks("apple");

    assertEquals(1, results.size());
    assertEquals(appleStock, results.getFirst());
  }

  @Test
  void findStocksSearchesSymbolAndCompany() {
    // Search "Go" should find both GOOGL and Wells Fargo
    List<Stock> results = exchange.findStocks("Go");

    assertEquals(2, results.size());
    assertTrue(results.contains(googleStock)); // GOOGL contains "Go"
    assertTrue(results.contains(wellsFargoStock)); // Wells Fargo contains "go"
  }

  @Test
  void findStocksWithNoMatchReturnsEmptyList() {
    List<Stock> results = exchange.findStocks("Microsoft");

    assertTrue(results.isEmpty());
  }

  @Test
  void findStocksWthNullSearchTerm() {
    assertThrows(IllegalArgumentException.class, () -> exchange.findStocks(null));
  }

  @Test
  void findStocksWithEmptySearchTerm() {
    assertThrows(IllegalArgumentException.class, () -> exchange.findStocks(""));
  }

  @Test
  void buyWithValidParametersCreatesPurchaseAndCommits() {
    final BigDecimal initialMoney = player.getCurrentMoney();
    final int initialPortfolioSize = player.getPortfolio().size();

    Transaction transaction = exchange.buy("AAPL", new BigDecimal("10"), player);

    assertNotNull(transaction);
    assertInstanceOf(Purchase.class, transaction);
    assertTrue(transaction.isCommitted());
    assertEquals("AAPL", transaction.getShare().stock().getSymbol());
    assertEquals(new BigDecimal("10"), transaction.getShare().quantity());

    // Money decreased
    assertTrue(player.getCurrentMoney().compareTo(initialMoney) < 0);

    // Portfolio increased
    assertEquals(initialPortfolioSize + 1, player.getPortfolio().size());
  }

  @Test
  void buyWithInvalidStock() {
    assertThrows(IllegalArgumentException.class, () ->
        exchange.buy("MSFT", new BigDecimal("10"), player));
  }

  @Test
  void buyWithNullPlayer() {
    assertThrows(IllegalArgumentException.class, () ->
        exchange.buy("AAPL", new BigDecimal("10"), null));
  }

  @Test
  void buyWithInsufficientFunds() {
    Player poorPlayer = new Player("Poor Player", new BigDecimal("10.00"));

    assertThrows(IllegalStateException.class, () ->
        exchange.buy("AAPL", new BigDecimal("1000"), poorPlayer));
  }

  @Test
  void sellWithValidParametersCreatesSaleAndCommits() {
    // First buy a share
    exchange.buy("AAPL", new BigDecimal("10"), player);

    // Get the share from portfolio
    Share share = player.getPortfolio().getShares("AAPL").getFirst();

    final BigDecimal moneyBeforeSale = player.getCurrentMoney();
    final int portfolioSizeBeforeSale = player.getPortfolio().size();

    Transaction transaction = exchange.sell(share, player);

    assertNotNull(transaction);
    assertInstanceOf(Sale.class, transaction);
    assertTrue(transaction.isCommitted());

    // Money increased
    assertTrue(player.getCurrentMoney().compareTo(moneyBeforeSale) > 0);

    // Portfolio decreased
    assertEquals(portfolioSizeBeforeSale - 1, player.getPortfolio().size());
  }

  @Test
  void sellWithNullShare() {
    assertThrows(IllegalArgumentException.class, () -> exchange.sell(null, player));
  }

  @Test
  void sellWithNullPlayer() {
    exchange.buy("AAPL", new BigDecimal("10"), player);
    Share share = player.getPortfolio().getShares("AAPL").getFirst();

    assertThrows(IllegalArgumentException.class, () -> exchange.sell(share, null));
  }

  @Test
  void sellWhenPlayerDoesNotOwnShare() {
    // Create share but don't add to player's portfolio
    Share share = new Share(appleStock, new BigDecimal("10"), new BigDecimal("150.00"));

    assertThrows(IllegalStateException.class, () -> exchange.sell(share, player));
  }

  @Test
  void advanceIncrementsWeek() {
    assertEquals(1, exchange.getWeek());

    exchange.advance();
    assertEquals(2, exchange.getWeek());

    exchange.advance();
    assertEquals(3, exchange.getWeek());
  }

  @Test
  void advanceUpdatesPrices() {
    BigDecimal originalPrice = appleStock.getSalesPrice();

    exchange.advance();

    BigDecimal newPrice = appleStock.getSalesPrice();
    assertNotEquals(originalPrice, newPrice);
  }

  @Test
  void advanceUpdatesAllStockPrices() {
    final BigDecimal appleOriginal = appleStock.getSalesPrice();
    BigDecimal googleOriginal = googleStock.getSalesPrice();
    BigDecimal wellsFargoOriginal = wellsFargoStock.getSalesPrice();

    exchange.advance();

    // All prices should change
    assertNotEquals(wellsFargoOriginal, wellsFargoStock.getSalesPrice());
    assertNotEquals(googleOriginal, googleStock.getSalesPrice());
    assertNotEquals(appleOriginal, appleStock.getSalesPrice());

  }

  @Test
  void advancePriceNeverGoesBelowOne() {
    // Create stock with very low price
    Stock pennyStock = new Stock("PENNY", "Penny Stock Inc.", new BigDecimal("1.50"));
    List<Stock> lowPriceStocks = new ArrayList<>();
    lowPriceStocks.add(pennyStock);
    Exchange testExchange = new Exchange("TEST", lowPriceStocks);

    // Advance many times to try to drive price below 1
    for (int i = 0; i < 100; i++) {
      testExchange.advance();
    }

    // Price should never go below 1
    assertTrue(pennyStock.getSalesPrice().compareTo(BigDecimal.ONE) >= 0);
  }

  @Test
  void advanceMultipleWeeksTracksCorrectly() {
    for (int i = 1; i <= 10; i++) {
      assertEquals(i, exchange.getWeek());
      exchange.advance();
    }
    assertEquals(11, exchange.getWeek());
  }

  @Test
  void buyAndSellWorkflowCompletesSuccessfully() {
    BigDecimal startingMoney = player.getCurrentMoney();

    // Week 1: Buy Apple stock
    Transaction purchase = exchange.buy("AAPL", new BigDecimal("10"), player);
    assertTrue(purchase.isCommitted());
    assertTrue(player.getCurrentMoney().compareTo(startingMoney) < 0);

    // Advance to week 2 (price changes)
    exchange.advance();
    assertEquals(2, exchange.getWeek());

    // Week 2: Sell the Apple stock
    Share share = player.getPortfolio().getShares("AAPL").getFirst();
    Transaction sale = exchange.sell(share, player);
    assertTrue(sale.isCommitted());

    // Player should have 0 shares now
    assertTrue(player.getPortfolio().isEmpty());

    // Transaction archive should have 2 transactions
    assertEquals(2, player.getTransactionArchive().getAllTransactions().size());
  }

  @Nested
  class GainersAndLosersTests {

    @BeforeEach
    void addPrices() {
      // advance prices so getLatestPriceChange() has two prices to work with
      appleStock.addNewSalesPrice(new BigDecimal("200.00")); // went up
      googleStock.addNewSalesPrice(new BigDecimal("2500.00")); // went down
      wellsFargoStock.addNewSalesPrice(new BigDecimal("20.00")); // went down
    }

    @Test
    void getGainersReturnsStocksWithPositiveChange() {
      List<Stock> gainers = exchange.getGainers(5);
      assertTrue(gainers.contains(appleStock));
      assertFalse(gainers.contains(googleStock));
      assertFalse(gainers.contains(wellsFargoStock));
    }

    @Test
    void getGainerRespectsLimit() {
      List<Stock> gainers = exchange.getGainers(1);
      assertEquals(1, gainers.size());
    }

    @Test
    void getGainersWithLimitLargerThanGainerCountReturnsAllGainers() {
      List<Stock> gainers = exchange.getGainers(10);
      assertEquals(1, gainers.size());
    }

    @Test
    void getLosersReturnsStocksWithNegativeChange() {
      List<Stock> losers = exchange.getLosers(5);
      assertTrue(losers.contains(googleStock));
      assertTrue(losers.contains(wellsFargoStock));
      assertFalse(losers.contains(appleStock));
    }

    @Test
    void getLosersRespectsLimit() {
      List<Stock> losers = exchange.getLosers(1);
      assertEquals(1, losers.size());
    }

    @Test
    void getLosersWithLimitLargerThanLoserCountReturnsAllLosers() {
      List<Stock> losers = exchange.getLosers(10);
      assertEquals(2, losers.size());
    }
  }
}
