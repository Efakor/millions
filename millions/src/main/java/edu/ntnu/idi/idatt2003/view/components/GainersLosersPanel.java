package edu.ntnu.idi.idatt2003.view.components;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



/**
 * Panel displaying the top gaining and losing stocks on the exchange.
 * Implements {@link GameObserver} to automatically refresh after each week advance.
 * The number of stocks displayed is configurable via a spinner control.
 */
public class GainersLosersPanel extends VBox implements GameObserver {
  private ExchangeController exchangeController;
  private VBox gainersList;
  private VBox losersList;
  private Spinner<Integer> limitSpinner;

  /**
   * Constructs the GainersLosersPanel and registers it as Game Observer.
   * Initialises the spinner,gainers list and losers list.
   *
   * @param exchangeController the exchange controller used to fetch gainer and losers
   */
  public GainersLosersPanel(ExchangeController exchangeController) {
    setSpacing(10);
    setMaxWidth(500);
    setPrefWidth(500);
    setPadding(new Insets(10));

    this.limitSpinner = new Spinner<>(1, 20, 5);
    gainersList = new VBox(5);
    losersList = new VBox(5);
    gainersList.setPrefWidth(300);
    losersList.setPrefWidth(300);
    this.exchangeController = exchangeController;
    // Titles
    Label gainersTitle = new Label("TOP GAINERS");
    gainersTitle.setStyle("-fx-text-fill:green; -fx-font-weight:bold");
    gainersList.getChildren().add(gainersTitle);
    Label loserTitle = new Label("LOSERS");
    loserTitle.setStyle("-fx-text-fill:red; -fx-font-weight:bold");
    losersList.getChildren().add(loserTitle);
    // Add to the HBox
    HBox lists = new HBox(10, gainersList, losersList);
    getChildren().addAll(limitSpinner, lists);
    limitSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
      refresh();
    });
    exchangeController.getExchange().addObserver(this);
    refresh();
  }

  /**
   * Refreshes the gainer and losers lists based on the spinner value.
   * Clears all existing rows except the title labels and rebuilds them
   * with latest stock data from the exchange.
   * Calculates both absolutes price change and percentage price for each stock.
   */
  private void refresh() {
    // To show minimum stock
    int limit = limitSpinner.getValue();
    losersList.getChildren().subList(1, losersList.getChildren().size()).clear();
    gainersList.getChildren().subList(1, gainersList.getChildren().size()).clear();
    exchangeController.getGainers(limit).forEach(stock ->
        gainersList.getChildren().add(buildGainerRow(stock)));
    exchangeController.getLosers(limit).forEach(stock ->
        losersList.getChildren().add(buildLoserRow(stock)));


  }


  /**
   * Builds a single row for the losers list.
   *
   * @param stock the stock to display
   * @return an HBox containing the stock information
   */

  private HBox buildGainerRow(Stock stock) {
    return buildRow(stock, true);

  }

  /**
   * Builds a single row for the losers list.
   *
   * @param stock the stock to display
   * @return an HBox containing the stock information
   */

  private HBox buildLoserRow(Stock stock) {
    return buildRow(stock, false);
  }
  /**
   * Builds a stock row with rank,symbol,company,price change and percentage.
   *
   * @param stock the stock to display
   * @return an HBox containing the stock information
   */

  private HBox buildRow(Stock stock, boolean isGainer) {
    String color = isGainer ? "green" : "red";
    String prefix = isGainer ? "+" : "";

    Label absolutePriceChange = new Label(stock.getLatestPriceChange()
        .setScale(2, RoundingMode.HALF_UP).toString());
    absolutePriceChange.setStyle("-fx-font-size:11px ;-fx-text-fill:" + color + ";");

    Label symbol = new Label(stock.getSymbol());
    symbol.setStyle("-fx-font-size: 11px; -fx-text-fill:white");

    String companyName = stock.getCompany();
    if (companyName.length() > 10) {
      companyName = companyName.substring(0, 10) + "...";
    }
    Label company = new Label(companyName);
    company.setStyle("-fx-font-size: 11px; -fx-text-fill:white");

    BigDecimal now = stock.getSalesPrice();
    BigDecimal change = stock.getLatestPriceChange();
    BigDecimal difference = now.subtract(change);

    BigDecimal percentage = change.divide(difference, 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));

    Label percentChangeLabel = new Label(String.format("%.1f%%", percentage.doubleValue()));
    percentChangeLabel.setStyle("-fx-font-size: 11px ; -fx-text-fill:" + ";");

    HBox row = new HBox(10, symbol, company, absolutePriceChange, percentChangeLabel);
    return row;

  }
  /**
   * Called by the exchange when a game event if fired.
   * Refreshes the panel when the week advances.
   *
   * @param event the event that was triggered
   */

  @Override
  public void onGameEvent(GameEvent event) {
    if (event == GameEvent.WEEK_ADVANCED) {
      refresh();


    }
  }
}



