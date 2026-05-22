package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Player;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.repository.CsvStockReader;
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
  public BigDecimal getTotalTax() {
    return player.getPortfolio().getTotalTax();
  }
  public BigDecimal getTotalCommission() {
    return player.getPortfolio().getTotalCommission();
  }
  public BigDecimal getTotalGross (){
    return player.getPortfolio().getTotalgross();
  }
  public String getPlayerName() {
    return player.getName();
  }
  public BigDecimal getNetWorth() {
    return player.getNetWorth();

  }
  public String getPlayerStatus() {
    return player.getStatus();
  }
  public BigDecimal getProfitLoss() {
    return player.getCurrentMoney().subtract(player.getStartingMoney());
  }
  public BigDecimal getStartingCapital(){
    return player.getStartingMoney();
  }

}



