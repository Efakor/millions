package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import edu.ntnu.idi.idatt2003.repository.CsvStockReader;
import javafx.application.Platform;

/**
 * Controller for Exchange operations.
 * Handles buying, selling, and market operations.
 */
public class ExchangeController {
  private final Exchange exchange;
  private final PlayerController playerController;


  public ExchangeController(Exchange exchange,PlayerController playerController) {
    this.exchange = exchange;
    this.playerController=playerController;
  }

  public Exchange getExchange() {
    return exchange;
  }

  public Transaction buy(String symbol, BigDecimal quantity) {
    return exchange.buy(symbol,quantity,playerController.getPlayer());

  }
  public void sell(Share share) {
    exchange.sell(share,playerController.getPlayer());
  }
  public void advance(){
    Platform.runLater(()->exchange.advance());
  }
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
  public List <Stock> getAllStocks() {
    return exchange.getAllStocks();
  }
  public List<Stock> findStocks (String searchTerm){
    return exchange.findStocks(searchTerm);
  }
  public List <Stock> getGainers(int limit){
    return exchange.getGainers(limit);
  }
  public List <Stock> getLosers(int limit){
    return exchange.getLosers(limit);
  }
}



