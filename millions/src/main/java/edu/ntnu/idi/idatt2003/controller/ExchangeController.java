package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import edu.ntnu.idi.idatt2003.controller.PlayerController;

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
    return exchange.getGainers(limit);
  }
  public List <Stock> getLosers(int limit){
    return exchange.getLosers(limit);
  }
}
