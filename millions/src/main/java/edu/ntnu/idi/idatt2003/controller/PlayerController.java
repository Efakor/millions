package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.*;
import edu.ntnu.idi.idatt2003.repository.CsvStockReader;

import javax.swing.text.Position;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
   * Controller for player operations.
   * Acts as the medium between the view layer and the Player model.
   * Handles game initialisation, player state, and end game operations.
   */

  public class PlayerController {

  private Exchange exchange;
  private Player player;


  public PlayerController() {
  }

  public void newGame(String name, BigDecimal capital, File stockFile) {
    this.player = new Player(name, capital);
    this.exchange = new Exchange(name);

    CsvStockReader reader = new CsvStockReader();
    try {
      List<Stock> stocks = reader.readStocksFromFile(stockFile.getPath());
      System.out.println("Loaded stocks: " + stocks.size());

      for (Stock stock : stocks) {
        exchange.addStock(stock);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load stock data", e);
    }
  }

  public Player getPlayer() {
    return player;
  }

  public Exchange getExchange() {
    return exchange;
  }

  public void sellAll() {
    List<Share> shares = new ArrayList<>(player.getPortfolio().getAllShares());
    for (Share share : shares) {
      exchange.sell(share, player);

    }
  }

  public BigDecimal getTotalProceeds() {
    return player.getPortfolio().getNetWorth();


  }
  public BigDecimal getTotalNetWorth() {
    return player.getPortfolio().getNetWorth();
  }
  public BigDecimal getTotalTax() {
    return player.getPortfolio().getTotalTax();
  }
  public BigDecimal getTotalCommission() {
    return player.getPortfolio().getTotalCommission();
  }
  public BigDecimal getTotalGross (){
    return player.getPortfolio().getTotalgross();
  }

}



