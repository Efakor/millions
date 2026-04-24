package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for Exchange operations.
 * Handles buying, selling, and market operations.
 */
public class ExchangeController {
  private final Exchange exchange;

  public ExchangeController(Exchange exchange) {
    this.exchange = exchange;
  }

  public Exchange getExchange() {
    return exchange;
  }

  public void buy(String symbol, BigDecimal qty) {
    throw new UnsupportedOperationException("Not supported yet.");

  }
  public void sell(Share share) {
    throw  new UnsupportedOperationException("Not supported yet.");
  }
  public void advance(){
    exchange.advance();
  }
  public void loadStocks(File f){
    throw new UnsupportedOperationException("Not supported yet.");
  }
  public void findStocks(String searchTerm){
    throw new UnsupportedOperationException("Not supported yet.");
  }
  public List <Stock> getGainers(int limit){
    throw new UnsupportedOperationException("Not supported yet.");
  }
  public List <Stock> getLosers(int limit){
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
