package edu.ntnu.idi.idatt2003.controller;

import edu.ntnu.idi.idatt2003.model.Player;
import edu.ntnu.idi.idatt2003.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerControllerTest {
  private PlayerController playerController;
  private ExchangeController exchangeController;
  private Player player;

  @BeforeEach
  void setUp() {
    playerController = new PlayerController();
    playerController.newGame("Kari",new BigDecimal("10000"));
    playerController.getExchange().addStock(
        new Stock("AAPL","Apple",new BigDecimal("150")));
    exchangeController=new ExchangeController(playerController.getExchange(),playerController);
    player = playerController.getPlayer();
  }
  @Nested
  @DisplayName("Tests for NewGameTests")
  class NewGameTests {
    @Test
    void playerNameValid() {
      //Arrange done in setup()

      //Assert
      assertEquals("Kari",playerController.getPlayerName());
    }
    @Test
    void playerCapitalValid() {
      //Arrange done in setup()

      //Assert
      assertEquals(new BigDecimal("10000"),playerController.getStartingCapital());

    }
    @Test
    void playerPortfolioValid() {
      //Arrange done in setup()

      //Assert
      assertTrue(player.getPortfolio().getAllShares().isEmpty());
    }

  }
  @Nested
  @DisplayName("Tests for SellAllTests")
  class SellAllTests {
    @Test
    void sellAll_buySharesValid() {
      //Arrange
      exchangeController.buy("AAPL",new BigDecimal("2"));
      //Act
      playerController.sellAll();
      //Assert
      assertTrue(player.getPortfolio().getAllShares().isEmpty());

    }
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
    @Test
    void emptyPortfolio(){
      //Assert
      assertDoesNotThrow(()->playerController.sellAll());
    }

  }
  @Nested
  @DisplayName("Tests for ProfitLossTests")
  class ProfitLossTests {
    @Test
    void profitLossStartZero() {
      assertEquals(BigDecimal.ZERO,playerController.getProfitLoss());
    }
    @Test
    void profitLossCheckBuying() {
      exchangeController.buy("AAPL",new BigDecimal("2"));
      playerController.sellAll();
      assertTrue(playerController.getProfitLoss().compareTo(BigDecimal.ZERO)<0);

    }

  }



}
