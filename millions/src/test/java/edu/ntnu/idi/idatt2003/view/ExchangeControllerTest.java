package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ExchangeControllerTest {
  private ExchangeController exchangeController;
  private PlayerController playerController;
  private Exchange exchange;
  private Player player;

  @BeforeEach
  void setUp() {
    playerController=new PlayerController();
    playerController.newGame("Kari",new BigDecimal("10000"));
   playerController.getExchange().addStock(new Stock("AAPL","Apple",new BigDecimal("150")));
   exchangeController=new ExchangeController(playerController.getExchange(),playerController);
   player=playerController.getPlayer();
   exchange=playerController.getExchange();
  }
  @Nested
  class BuyTests{
    @Test
    void buyValidPlayerShare() {
      //Arrange
      BigDecimal quantity=new BigDecimal("2");
      //Act
      exchangeController.buy("AAPL",quantity);
      //Assert
      assertFalse(player.getPortfolio().getAllShares().isEmpty());
    }
    @Test
    void buyValidMoneyDeduction() {
      //Arrange
      BigDecimal before=player.getCurrentMoney();
      BigDecimal quantity=new BigDecimal("2");
      //Act
      exchangeController.buy("AAPL",quantity);
      //Assert
      assertTrue(player.getCurrentMoney().compareTo(before)<0);
    }
    @Test
    void buyInsufficientFunds_throwsException() {
      //Arrange
      BigDecimal quantity=new BigDecimal("99999");
      //Assert
      assertThrows(IllegalStateException.class, () ->
          exchangeController.buy("AAPL",quantity));
    }
    @Test
    void buyInvalidSymbol_throwsException() {
      //Assert
      assertThrows(IllegalArgumentException.class, () ->
          exchangeController.buy("Invalid",new BigDecimal("99999")));
    }




  }
  @Nested
  class SellTests {
    @Test
    void sellValidSymbol_shareRemovedPortfolio() {
      //Arrange
      exchangeController.buy("AAPL",new BigDecimal("2"));
      Share share=player.getPortfolio().getAllShares().get(0);
      //Act
      exchangeController.sell(share);
      //Assert
      assertTrue(player.getPortfolio().getAllShares().isEmpty());

    }
    @Test
    void sellValid_moneyIncreasedAfterSell() {
      //Arrange
      exchangeController.buy("AAPL",BigDecimal.ONE);
      BigDecimal moneyAfter=player.getCurrentMoney();
      Share share=player.getPortfolio().getAllShares().get(0);
      //Act
      exchangeController.sell(share);
      //Assert
      assertTrue(player.getCurrentMoney().compareTo(moneyAfter)>0);
    }


  }
  @Nested
  class SearchTests {
    @Test
    void searchForStock() {
      //Arrange
      String search="Apple";
      List<Stock> stocks=exchangeController.findStocks(search);
      assertFalse(stocks.isEmpty());
      assertEquals("AAPL",stocks.get(0).getSymbol());
    }
    @Test
    void searchStock_noMatch(){
      //Arrange
      String search="RRRRR";
      //Act
      List<Stock> stocks=exchangeController.findStocks(search);
      //Assert
      assertTrue(stocks.isEmpty());
    }


    }
    @Nested
    class advanceTests {
    @Test
    void advanceWeekIncrements() {
      //Arrange
      int weekBefore=exchange.getWeek();
      //Act
      exchange.advance();
      //Assert
      assertEquals(weekBefore+1,exchange.getWeek());
    }
    @Test
    void advanceWeek_priceHistoryGrows() {
      //Arrange
      int historySizeBefore=exchange.getStock("AAPL").getHistoricalPrices().size();
      //Act
      exchange.advance();
      //Assert
      assertTrue(exchange.getStock("AAPL").getHistoricalPrices().size()>historySizeBefore);
    }
    }

  }


