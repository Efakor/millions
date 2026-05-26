package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.repository.CsvStockReader;
import edu.ntnu.idi.idatt2003.model.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for Exchange operations.
 * Handles buying, selling, and market operations.
 */
public class ExchangeController {
  private final Exchange exchange;
  private final PlayerController playerController;
  /**
   * Constructs the exchange controller with the given exchange and player controller.
   *
   * @param exchange         the exchange model to control
   * @param playerController the player controller providing the current player
   */


  public ExchangeController(Exchange exchange, PlayerController playerController) {
    this.exchange = exchange;
    this.playerController = playerController;
  }
  /**
   * Returns the underlying exchange model.
   *
   * @return the exchange
   */

  public Exchange getExchange() {
    return exchange;
  }

  /**
   * Buys shares of the given stock symbol for the current player.
   *
   * @param symbol   the stock symbol to buy
   * @param quantity the number of shares to purchase
   * @return the completed purchase transaction
   * @throws IllegalArgumentException if the symbol is not found
   * @throws IllegalStateException    if the player has insufficient funds
   */

  public Transaction buy(String symbol, BigDecimal quantity) {
    return exchange.buy(symbol,quantity, playerController.getPlayer());

  }
  /**
   * Sells the given share for the current player.
   *
   * @param share the share to sell
   * @throws IllegalArgumentException if the share or player is null
   */

  public void sell(Share share) {
    exchange.sell(share,playerController.getPlayer());
  }

  /**
   * Advances the exchange to the next trading week.
   * Executed on the JavaFX application thread via Platform.runLater.
   */

  public void advance(){exchange.advance();
  }

  /**
   * Loads stocks from the given CSV file into the exchange.
   *
   * @param f the CSV file containing stock data
   * @throws RuntimeException if the file cannot be read
   */

  public void loadStocks(File f){
    CsvStockReader reader = new CsvStockReader();
    try {
      List<Stock> stocks = reader.readStocksFromFile(f.getPath());
      System.out.println("Loaded stocks: " + stocks.size());

      for (Stock stock : stocks) {
        exchange.addStock(stock);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load stock data", e);
    }
  }
  /**
   * Returns all stocks currently listed on the exchange.
   *
   * @return an unmodifiable list of all stocks
   */
  public List <Stock> getAllStocks() {
    return exchange.getAllStocks();
  }
  /**
   * Finds stocks matching the given search term by symbol or company name.
   *
   * @param searchTerm the term to search for
   * @return a list of matching stocks, empty if none found
   * @throws IllegalArgumentException if the search term is null or empty
   */
  public List<Stock> findStocks (String searchTerm) {
    return exchange.findStocks(searchTerm);
  }
  /**
   * Returns the top gaining stocks since the last week advance.
   *
   * @param limit the maximum number of stocks to return
   * @return a list of top gaining stocks sorted by price change descending
   */

  public List <Stock> getGainers(int limit) {
    return exchange.getGainers(limit);
  }
  /**
   * Returns the top gaining stocks since the last week advance.
   *
   * @param limit the maximum number of stocks to return
   * @return a list of top gaining stocks sorted by price change descending
   */

  public List <Stock> getLosers(int limit) {
    return exchange.getLosers(limit);

  }
}



