package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.Player;
import edu.ntnu.idi.idatt2003.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PlayerController.
 * Test game initialisation, sell all and profit/loss calculations.
 */

public class PlayerControllerTest {

  private PlayerController playerController;
  private ExchangeController exchangeController;
  private Player player;

  /**
   * Set up fresh player, exchange and controller before each test.
   */

  @BeforeEach
  void setUp() {
    playerController = new PlayerController();
    playerController.newGame("Kari",new BigDecimal("10000"));
    playerController.getExchange().addStock(
        new Stock("AAPL","Apple",new BigDecimal("150")));
    exchangeController=new ExchangeController(playerController.getExchange(),playerController);
    player = playerController.getPlayer();
  }

  /**
   * Tests for NewGameTests
   */
  @Nested
  @DisplayName("Tests for NewGameTests")
  class NewGameTests {
    /**
     * Tests player name is correct.
     */
    @Test
    void playerNameValid() {
      //Arrange done in setup()

      //Assert
      assertEquals("Kari",playerController.getPlayerName());
    }

    /**
     * Tests player capital is correct
     */
    @Test
    void playerCapitalValid() {
      //Arrange done in setup()

      //Assert
      assertEquals(new BigDecimal("10000"),playerController.getStartingCapital());

    }

    /**
     * Tests portfolio starts empty
     */
    @Test
    void playerPortfolioValid() {
      //Arrange done in setup()

      //Assert
      assertTrue(player.getPortfolio().getAllShares().isEmpty());
    }

  }

  /**
   * Tests for all sellAllTests
   */
  @Nested
  @DisplayName("Tests for SellAllTests")
  class SellAllTests {
    /**
     * Tests for buy some shares first
     */
    @Test
    void sellAll_buySharesValid() {
      //Arrange
      exchangeController.buy("AAPL",new BigDecimal("2"));
      //Act
      playerController.sellAll();
      //Assert
      assertTrue(player.getPortfolio().getAllShares().isEmpty());

    }

    /**
     * Tests money increased after sell
     */
    @Test
    void moneyIncreasesAfterSell(){
      //Arrange
      exchangeController.buy("AAPL",new BigDecimal("2"));
      BigDecimal moneyAfterBuy=player.getCurrentMoney();
      //Act
      playerController.sellAll();
      //Assert
      assertTrue(player.getCurrentMoney().compareTo(moneyAfterBuy)>0);
    }

    /**
     * Tests sell all on empty portfolio
     */
    @Test
    void emptyPortfolio(){
      //Assert
      assertDoesNotThrow(()->playerController.sellAll());
    }

  }

  /**
   * Tests for Profit/loss
   */
  @Nested
  @DisplayName("Tests for ProfitLossTests")
  class ProfitLossTests {
    /**
     * Tests for start profit/loss should be zero
     */
    @Test
    void profitLossStartZero() {
      //Arrange done in setup()
      assertEquals(BigDecimal.ZERO,playerController.getProfitLoss());
    }

    /**
     * Tests for after buying and sell check profit/loss changed.
     */
    @Test
    void profitLossCheckBuying() {
      //Arrange
      exchangeController.buy("AAPL",new BigDecimal("2"));
      //Act
      playerController.sellAll();
      //Assert
      assertTrue(playerController.getProfitLoss().compareTo(BigDecimal.ZERO)<0);

    }

  }



}
