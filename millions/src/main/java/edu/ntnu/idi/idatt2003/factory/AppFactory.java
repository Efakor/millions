package edu.ntnu.idi.idatt2003.factory;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.view.*;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;

public class AppFactory {

  public static MainView buildMainView(PlayerController playerController, String name, BigDecimal capital, File file,Stage stage) {
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
    portfolioView.setSellAllRequest(()->{
      SellAllDialog.show(exchangeController,playerController,stage,()->{

      int weeks=exchangeController.getExchange().getWeek();
      SummaryView summaryView=new SummaryView(playerController,weeks,stage);
      stage.getScene().setRoot(summaryView);
      stage.setFullScreen(true);
      stage.requestFocus();
      });
    });

    //Gainer/Losers panel
    GainersLosersPanel gainersLosersPanel = new GainersLosersPanel(exchangeController);
    exchangeController.getExchange().addObserver(gainersLosersPanel);
    //Wire into layout
    VBox rightPanel = new VBox(10, gainersLosersPanel, portfolioView);
    VBox.setVgrow(mainView, javafx.scene.layout.Priority.ALWAYS);
    mainView.setStockListView(stockListView);
    mainView.setStockDetailView(stockDetailPanel);
    mainView.setPortfolioView(rightPanel);

    // in AppFactory, after building mainView
    mainView.prefHeightProperty().bind(stage.heightProperty());
    mainView.prefWidthProperty().bind(stage.widthProperty());
    return mainView;

  }
}
