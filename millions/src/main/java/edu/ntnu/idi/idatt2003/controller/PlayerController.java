package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Player;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.repository.CsvStockReader;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    public void newGame(String name, BigDecimal capital, File stockFile){
        Objects.requireNonNull(name,"Player name cannot be null");
        Objects.requireNonNull(capital,"Starting capital cannot be null");
        Objects.requireNonNull(stockFile,"Stock file cannot be null");
        if(!stockFile.exists()|| !stockFile.isFile()){
          throw new IllegalArgumentException("Invalid stock file"+stockFile.getName());
        }
        try{
          player=new Player(name,capital);
          CsvStockReader reader=new CsvStockReader();
          List<Stock> stocks=reader.readStocksFromFile(stockFile.getPath());
          if(stocks==null ||stocks.isEmpty()){
            throw new IllegalArgumentException("No stocks loaded from file"+stockFile.getName());
          }
          exchange=new Exchange("Millions Exchange",stocks);
        }catch(IOException e) {
          throw new IllegalStateException("Failed to load stock file" + stockFile.getName(),e);
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


