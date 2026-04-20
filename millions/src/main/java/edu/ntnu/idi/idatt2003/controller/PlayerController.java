package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Player;
import java.io.File;
import java.math.BigDecimal;
  /**
   * Controller for player operations.
   * Acts as the medium between the view layer and the Player model.
   * Handles game initialisation, player state, and end game operations.
   */

  public class PlayerController {

    private final Exchange exchange;
    private final Player player;

    public PlayerController(Exchange exchange, Player player) {
      this.exchange = exchange;
      this.player = player;
    }

    public void newGame(String name, BigDecimal capital, File stockFile){
      throw new UnsupportedOperationException("Not supported yet.");

    }
    public Player getPlayer(){
      return player;
    }
    public void sellAll(){
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }


