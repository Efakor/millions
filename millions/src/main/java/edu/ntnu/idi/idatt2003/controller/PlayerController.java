package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Player;
import edu.ntnu.idi.idatt2003.model.Share;
import java.io.File;
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

  /**
   * Constructs a new PlayerController with no active game session.
   */

  public PlayerController() {
  }

  /**
   * Starts a new game session with the given name and capital.
   * Creates a new Player and Exchange. Used directly by unit tests.
   *
   * @param name    the player's name
   * @param capital the starting capital
   */

  public void newGame(String name, BigDecimal capital, File stockFile) {
    newGame(name, capital);

  }

  /**
   * Starts a new game session with the given name and capital.
   * Creates a new Player and Exchange. Used directly by unit tests.
   *
   * @param name    the player's name
   * @param capital the starting capital
   */

  public void newGame(String name, BigDecimal capital) {
    this.player = new Player(name, capital);
    this.exchange = new Exchange(name);
  }
  /**
   * Returns the current player.
   *
   * @return the player
   */

  public Player getPlayer() {
    return player;
  }
  /**
   * Returns the current exchange.
   *
   * @return the exchange
   */

  public Exchange getExchange() {
    return exchange;
  }

  /**
   * Sells all shares in the player's portfolio.
   * Iterates over a copy of the portfolio to avoid concurrent modification.
   */
  public void sellAll() {
    List<Share> shares = new ArrayList<>(player.getPortfolio().getAllShares());
    for (Share share : shares) {
      exchange.sell(share, player);

    }
  }

  /**
   * Returns the estimated net proceeds from selling all holdings.
   *
   * @return the total net proceeds
   */

  public BigDecimal getTotalProceeds() {
    return player.getPortfolio().getNetWorth();


  }
  /**
   * Returns the total capital gains tax across all holdings.
   *
   * @return the total tax
   */

  public BigDecimal getTotalTax() {
    return player.getPortfolio().getTotalTax();
  }
  /**
   * Returns the total commission across all holdings.
   *
   * @return the total commission
   */

  public BigDecimal getTotalCommission() {
    return player.getPortfolio().getTotalCommission();
  }

  /**
   * Returns the total gross value across all holdings before fees.
   *
   * @return the total gross value
   */

  public BigDecimal getTotalGross() {
    return player.getPortfolio().getTotalGross();
  }

  /**
   * Returns the player's name.
   *
   * @return the player name
   */

  public String getPlayerName() {
    return player.getName();
  }

  /**
   * Returns the player's current net worth including cash and portfolio value.
   *
   * @return the net worth
   */

  public BigDecimal getNetWorth() {
    return player.getNetWorth();

  }

  /**
   * Returns the player's current status based on weeks played and net worth growth.
   *
   * @return the status string :Novice, Investor or Speculator
   */

  public String getPlayerStatus() {
    return player.getStatus();
  }

  /**
   * Returns the player's profit or loss compared to the starting capital.
   * A positive value indicates profit, negative indicates loss.
   *
   * @return the profit or loss amount
   */

  public BigDecimal getProfitLoss() {
    return player.getCurrentMoney().subtract(player.getStartingMoney());
  }

  /**
   * Returns the player's starting capital.
   *
   * @return the starting capital
   */

  public BigDecimal getStartingCapital() {
    return player.getStartingMoney();
  }

}



