package edu.ntnu.idi.idatt2003.controller;


import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;

import java.util.List;
import java.io.File;
import java.math.BigDecimal;
/**
 * Controller for Exchange operations.
 * Handles buying, selling, and market operations.
 */

public class ExchangeController {
  private final Exchange exchange;
  public ExchangeController(Exchange exchange) {
    this.exchange = exchange;

  }
  public void buy(String symbol, BigDecimal qty) {
    throw new UnsupportedOperationException("Not supported yet.");

  }
  public void sell(Share share) {
    throw  new UnsupportedOperationException("Not supported yet.");
  }
  public void advance(){
    throw new UnsupportedOperationException("Not supported yet.");
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
