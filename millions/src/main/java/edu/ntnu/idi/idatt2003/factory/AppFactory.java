package edu.ntnu.idi.idatt2003.factory;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.view.*;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.File;
import java.math.BigDecimal;

public class AppFactory {
  public static MainView buildMainView(PlayerController playerController, String name, BigDecimal capital, File file) {
    playerController.newGame(name, capital, file);
    ExchangeController exchangeController = new ExchangeController(playerController.getExchange(), playerController);
    MainView mainView = new MainView(exchangeController, playerController);
    //Stock list
    StockListView stockListView = new StockListView(playerController.getExchange());
    StockDetailPanel stockDetailPanel = new StockDetailPanel(exchangeController, playerController);
    stockListView.setDetailPanel(stockDetailPanel);
    stockListView.setupSelectionListener();

    //Portfolio(self registers in constructor)
    PortfolioView portfolioView = new PortfolioView(playerController, exchangeController);
    //Gainer/Losers panel
    GainersLosersPanel gainersLosersPanel = new GainersLosersPanel(exchangeController);
    exchangeController.getExchange().addObserver(gainersLosersPanel);
    //Wire into layout
    VBox rightPanel = new VBox(10, gainersLosersPanel, portfolioView);
    mainView.setStockListView(stockListView);
    mainView.setStockDetailView(stockDetailPanel);
    mainView.setPortfolioView(rightPanel);

    return mainView;

  }
}
