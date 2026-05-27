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
 * Factory class responsible for assembling the main game screen.
 * Creates and wires all controllers, views, and observer registrations
 * in one place so the rest of the application has no global state.
 */
public class AppFactory {

  /**
   * Builds and returns a fully wired {@link MainView} for the given player session.
   *
   * <p>Initialises the game via {@code PlayerController.newGame()}, constructs all
   * view components, registers GameObservers on Exchange, and binds the layout
   * to the stage dimensions.</p>
   *
   * @param playerController the player controller used to initialise the game
   * @param name             the player's name
   * @param capital          the player's starting capital
   * @param file             the CSV file containing stock data
   * @param stage            the primary JavaFX stage
   * @return a fully assembled {@link MainView} ready to be set as the scene root
   */
  public static MainView buildMainView(
      PlayerController playerController, String name, BigDecimal capital, File file, Stage stage) {
    playerController.newGame(name, capital, file);
    ExchangeController exchangeController =
        new ExchangeController(playerController.getExchange(), playerController);
    exchangeController.loadStocks(file);
    MainView mainView;
    mainView = new MainView(exchangeController, playerController);
    // Stock list
    StockListView stockListView = new StockListView(exchangeController);
    WatchlistView watchlistView = new WatchlistView(exchangeController);
    StockDetailPanel stockDetailPanel =
        new StockDetailPanel(exchangeController, playerController, watchlistView);
    stockListView.setDetailPanel(stockDetailPanel);
    stockListView.setupSelectionListener();

    // Portfolio(self registers in constructor)
    PortfolioView portfolioView = new PortfolioView(playerController, exchangeController);
    portfolioView.setSellAllRequest(() ->
        SellAllDialog.show(exchangeController, playerController, stage, () -> {

          int weeks = exchangeController.getExchange().getWeek();
          SummaryView summaryView = new SummaryView(playerController, weeks, stage);
          stage.getScene().setRoot(summaryView);
          stage.setFullScreen(true);
          stage.requestFocus();
        }));

    // Gainer/Losers panel
    GainersLosersPanel gainersLosersPanel = new GainersLosersPanel(exchangeController);
    exchangeController.getExchange().addObserver(gainersLosersPanel);
    // News Feed
    NewsFeedPanel newsFeedPanel = new NewsFeedPanel(exchangeController);
    // Wire into layout
    VBox rightPanel;
    rightPanel = new VBox(10, gainersLosersPanel, newsFeedPanel, watchlistView, portfolioView);
    VBox.setVgrow(mainView, javafx.scene.layout.Priority.ALWAYS);
    mainView.setStockListView(stockListView);
    mainView.setStockDetailView(stockDetailPanel);
    mainView.setPortfolioView(rightPanel);

    // In AppFactory, after building mainView
    mainView.prefHeightProperty().bind(stage.heightProperty());
    mainView.prefWidthProperty().bind(stage.widthProperty());
    return mainView;

  }
}
