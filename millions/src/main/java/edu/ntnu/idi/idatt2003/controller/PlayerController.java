package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Player;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.repository.CsvStockReader;

import java.io.IOException;
import java.math.BigDecimal;
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

    public void newGame(String name, BigDecimal capital){
      this.player=new Player(name, capital);
      this.exchange=new Exchange(name);

      CsvStockReader reader=new CsvStockReader();
      try {
        List<Stock> stocks = reader.readStocksFromFile("src/main/resources/stocks.csv/stocks.csv");

        for (Stock stock : stocks) {
          exchange.addStock(stock);
        }
      }catch (IOException e) {
        throw new RuntimeException("Failed to load stock data",e);
      }
    }
    public Player getPlayer(){
      return player;
    }
    public Exchange getExchange(){
      return exchange;
    }
    public void sellAll(){
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }


