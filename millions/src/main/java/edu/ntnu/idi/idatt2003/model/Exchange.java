package edu.ntnu.idi.idatt2003.model;

import edu.ntnu.idi.idatt2003.factory.TransactionFactory;
import edu.ntnu.idi.idatt2003.model.calculator.SaleCalculator;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import edu.ntnu.idi.idatt2003.observer.Observable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents a stock exchange where stocks are traded.
 * The exchange manages a collection of stocks, tracks the current trading week,
 * and provides methods to buy and sell stocks with automatic price updates.
 *
 * <p>Stock prices change randomly each week when advance() is called,
 * showing market volatility. The exchange executes transactions immediately
 * and handles all the complexity of creating transactions and committing them.</p>
 *
 * @author efakor
 * @version 1.0
 */
public class Exchange implements Observable {
  private final String name;
  private int week;
  private final Map<String, Stock> stocks;
  private final Random random;
  private final List<GameObserver> observers = new ArrayList<>();

  /**
   * Constructs a new Exchange with the given name and list of stocks.
   * The exchange starts at week 1.
   *
   * @param name   the name of the exchange (e.g. "NASDAQ")
   * @param stocks the initial list of stocks to add to the exchange
   * @throws IllegalArgumentException if name is null or empty
   */
  public Exchange(String name, List<Stock> stocks) {
    validateName(name);

    this.name = name;
    this.week = 1;
    this.stocks = new HashMap<>();
    this.random = new Random();

    if (stocks != null) {
      for (Stock stock : stocks) {
        addStock(stock);
      }
    }
  }

  /**
   * Constructs a new Exchange with the given name.
   * Convenience constructor for testing. Stocks can be added later.
   *
   * @param name the name of the exchange
   * @throws IllegalArgumentException if name is null or empty
   */
  public Exchange(String name) {
    this(name, null);
  }

  /**
   * Gets the name of the exchange.
   *
   * @return the exchange name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the current trading week.
   * The week number starts at 1 and increments with each call to advance().
   *
   * @return the current week number
   */
  public int getWeek() {
    return week;
  }

  /**
   * Adds a stock to the exchange.
   * The stock is stored in a map with its symbol as the key for fast lookup.
   *
   * @param stock the stock to add
   * @throws IllegalArgumentException if stock is null
   */
  public void addStock(Stock stock) {
    if (stock == null) {
      throw new IllegalArgumentException("Stock cannot be null");
    }
    stocks.put(stock.getSymbol(), stock);
  }

  /**
   * Checks if a stock with the given symbol exists in the exchange.
   *
   * @param symbol the stock symbol to check
   * @return true if the stock exists, false otherwise
   * @throws IllegalArgumentException if symbol is null or empty
   */
  public boolean hasStock(String symbol) {
    if (symbol == null || symbol.trim().isEmpty()) {
      throw new IllegalArgumentException("Symbol cannot be null or empty");
    }
    return stocks.containsKey(symbol);
  }

  /**
   * Gets a stock by its symbol.
   *
   * @param symbol the stock symbol (e.g., "AAPL", "GOOGL")
   * @return the stock, or null if not found
   * @throws IllegalArgumentException if symbol is null or empty
   */
  public Stock getStock(String symbol) {
    if (symbol == null || symbol.trim().isEmpty()) {
      throw new IllegalArgumentException("Symbol cannot be null or empty");
    }
    return stocks.get(symbol);
  }

  /**
   * Gets all stocks in the exchange.
   *
   * @return unmodifiable list of all stocks
   */
  public List<Stock> getAllStocks() {
    return List.copyOf(stocks.values());
  }

  /**
   * Finds stocks by search term, checking both symbol and company name.
   * Search is case-insensitive and matches partial strings.
   *
   * <p>Example: Searching for "Go" would find both "GOOGL - Alphabet Inc"
   * and "WFC - Wells Fargo".</p>
   *
   * @param searchTerm the term to search for
   * @return list of matching stocks (empty if no matches)
   * @throws IllegalArgumentException if searchTerm is null or empty
   */
  public List<Stock> findStocks(String searchTerm) {
    if (searchTerm == null || searchTerm.trim().isEmpty()) {
      throw new IllegalArgumentException("Company name cannot be null or empty");
    }
    String searchName = searchTerm.toLowerCase();
    return stocks.values().stream()
        .filter(stock ->
            stock.getCompany().toLowerCase().contains(searchName)
                || stock.getSymbol().toLowerCase().contains(searchName))
        .collect(Collectors.toList());
  }

  /**
   * Validates that the name is not null or empty.
   *
   * @param name the name to validate
   * @throws IllegalArgumentException if name is null or empty
   */
  private void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
  }

  /**
   * Creates and commits a sale transaction for a share.
   * The transaction is executed immediately: money is received,
   * the share is removed from the portfolio, and the transaction is recorded.
   *
   * <p>This is a convenience method that handles all the complexity
   * of creating a Sale and committing it in one call.</p>
   *
   * @param share  the share to sell
   * @param player the player making the sale
   * @return the completed sale transaction
   * @throws IllegalArgumentException if share or player is null, or stock not found in exchange
   * @throws IllegalStateException    if player doesn't own the share
   */
  public Transaction sell(Share share, Player player) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    Stock stock = getStock(share.stock().getSymbol());
    if (stock == null) {
      throw new IllegalArgumentException(
          "Stock not found in exchange: " + share.stock().getSymbol());
    }

    SaleCalculator calculator = new SaleCalculator(share);
    Sale sale = TransactionFactory.createSale(share, week);
    sale.commit(player);

    if (sale.isCommitted()) {
      notifyObservers(GameEvent.SALE_MADE);
    }

    return sale;
  }

  /**
   * Creates and commits a purchase transaction for a stock.
   * The transaction is executed immediately: money is withdrawn,
   * the share is added to the portfolio, and the transaction is recorded.
   *
   * <p>This is a convenience method that handles all the complexity
   * of creating a Share, Purchase, and committing it in one call.</p>
   *
   * @param symbol   the stock symbol to buy
   * @param quantity the number of shares to buy
   * @param player   the player making the purchase
   * @return the completed purchase transaction
   * @throws IllegalArgumentException if symbol is invalid, stock not found, or player is null
   * @throws IllegalStateException    if quantity is invalid or player has insufficient funds
   */
  public Transaction buy(String symbol, BigDecimal quantity, Player player) {
    Stock stock = getStock(symbol);
    if (stock == null) {
      throw new IllegalArgumentException("Stock not found in exchange: " + symbol);
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    BigDecimal currentPrice = stock.getSalesPrice();
    Share share = new Share(stock, quantity, currentPrice);

    Purchase purchase = TransactionFactory.createPurchase(share, week);
    purchase.commit(player);

    if (purchase.isCommitted()) {
      notifyObservers(GameEvent.PURCHASE_MADE);
    }

    return purchase;
  }

  /**
   * Advances to the next trading week and updates stock prices randomly.
   * Each stock's price changes by a random amount between -7% and +7%.
   *
   * <p>Price updates simulate market volatility. Prices are prevented
   * from falling below $1.00 to maintain realistic trading conditions.</p>
   */
  public void advance() {
    week++;

    // Update each stock's price randomly
    for (Stock stock : stocks.values()) {
      BigDecimal currentPrice = stock.getSalesPrice();

      // Random change between -7% and +7%
      double percentageChange = (random.nextDouble() * 0.10) - 0.07; // -0.07 to +0.07
      BigDecimal change = currentPrice.multiply(new BigDecimal(percentageChange));
      BigDecimal newPrice = currentPrice.add(change);

      // Ensure price doesn't go below $1
      if (newPrice.compareTo(BigDecimal.ONE) < 0) {
        newPrice = BigDecimal.ONE;
      }
      stock.addNewSalesPrice(newPrice);
    }

    notifyObservers(GameEvent.WEEK_ADVANCED);
  }

  /**
   * Returns the stocks with the biggest positive price change since last week.
   *
   * @param limit the maximum number of stocks to return
   * @return a list of top gaining stocks, sorted by price change descending
   */
  public List<Stock> getGainers(int limit) {
    return stocks.values().stream()
        .filter(stock -> stock.getLatestPriceChange().compareTo(BigDecimal.ZERO) > 0)
        .sorted((a, b) -> b.getLatestPriceChange().compareTo(a.getLatestPriceChange()))
        .limit(limit)
        .collect(Collectors.toList());
  }

  /**
   * Returns the stocks that have performed worst since last week.
   *
   * @param limit the maximum number of stocks to return
   * @return a list of worst performing stocks, sorted by price change ascending
   */
  public List<Stock> getLosers(int limit) {
    return stocks.values().stream()
        .filter(stock -> stock.getLatestPriceChange().compareTo(BigDecimal.ZERO) < 0)
        .sorted(Comparator.comparing(Stock::getLatestPriceChange))
        .limit(limit)
        .collect(Collectors.toList());
  }

  @Override
  public void addObserver(GameObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(GameObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers(GameEvent event) {
    for (GameObserver observer : observers) {
      observer.onGameEvent(event);
    }
  }
}
