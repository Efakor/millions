package edu.ntnu.idi.idatt2003.factory;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.view.GainersLosersPanel;
import edu.ntnu.idi.idatt2003.view.MainView;
import edu.ntnu.idi.idatt2003.view.NewsFeedPanel;
import edu.ntnu.idi.idatt2003.view.PortfolioView;
import edu.ntnu.idi.idatt2003.view.SellAllDialog;
import edu.ntnu.idi.idatt2003.view.StockDetailPanel;
import edu.ntnu.idi.idatt2003.view.StockListView;
import edu.ntnu.idi.idatt2003.view.SummaryView;
import edu.ntnu.idi.idatt2003.view.WatchlistView;
import java.io.File;
import java.math.BigDecimal;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Wires all controllers, views and observers together into a fully functional
 * main screen following the Factory design pattern.

 */

public class AppFactory {
  /**
   * Builds and returns the fully wired main game view.
   * Initialises a new game session, creates all controllers and views,
   * loads stock data from the CSV file, registers all GameObservers
   * and connects all components together.
   *
   * <p>The following components are created and wired:</p>
   * <ul>
   *   <li>{@link ExchangeController} — controls all market operations</li>
   *   <li>{@link StockListView} — searchable and filterable stock list</li>
   *   <li>{@link StockDetailPanel} — selected stock details and price chart</li>
   *   <li>{@link WatchlistView} — player's bookmarked stocks with alerts</li>
   *   <li>{@link PortfolioView} — current holdings with sell all button</li>
   *   <li>{@link GainersLosersPanel} — top gainers and losers</li>
   *   <li>{@link NewsFeedPanel} — random market event headlines</li>
   * </ul>
   *
   * @param playerController the player controller to initialise the game session with
   * @param name             the player's name
   * @param capital          the starting capital
   * @param file             the CSV file containing stock data
   * @param stage            the primary application stage
   * @return the fully assembled and wired {@link MainView}
   */

  public static MainView buildMainView(PlayerController playerController,
                                       String name, BigDecimal capital,
                                       File file, Stage stage) {
    playerController.newGame(name, capital, file);
    ExchangeController exchangeController = new ExchangeController(
        playerController.getExchange(), playerController);
    exchangeController.loadStocks(file);
    // Stock list
    StockListView stockListView = new StockListView(exchangeController);
    WatchlistView watchlistView = new WatchlistView(exchangeController);
    StockDetailPanel stockDetailPanel = new StockDetailPanel(
        exchangeController, playerController, watchlistView);
    stockListView.setDetailPanel(stockDetailPanel);
    stockListView.setupSelectionListener();

    // Portfolio(self registers in constructor)
    PortfolioView portfolioView = new PortfolioView(playerController, exchangeController);
    portfolioView.setSellAllRequest(() -> {
      SellAllDialog.show(exchangeController, playerController, stage, () -> {
        int weeks = exchangeController.getExchange().getWeek();
        SummaryView summaryView = new SummaryView(playerController, weeks, stage);
        stage.getScene().setRoot(summaryView);
        stage.setFullScreen(true);
        stage.requestFocus();
      });
    });

    // Gainer/Losers panel
    GainersLosersPanel gainersLosersPanel = new GainersLosersPanel(exchangeController);
    exchangeController.getExchange().addObserver(gainersLosersPanel);
    // News Feed
    NewsFeedPanel newsFeedPanel = new NewsFeedPanel(exchangeController);
    // Wire into layout
    VBox rightPanel = new VBox(10, gainersLosersPanel, newsFeedPanel, watchlistView, portfolioView);

    MainView mainView = new MainView(exchangeController, playerController);
    VBox.setVgrow(mainView, javafx.scene.layout.Priority.ALWAYS);
    mainView.setPortfolioView(rightPanel);
    mainView.setStockListView(stockListView);
    mainView.setStockDetailView(stockDetailPanel);


    // In AppFactory, after building mainView
    mainView.prefHeightProperty().bind(stage.heightProperty());
    mainView.prefWidthProperty().bind(stage.widthProperty());
    return mainView;

  }
}
