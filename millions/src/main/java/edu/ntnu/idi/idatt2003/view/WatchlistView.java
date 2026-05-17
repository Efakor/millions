package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Watchlist panel showing bookmarked stocks.
 * Registers as a GameObserver - after each WEEK_ADVANCED event,
 * checks if any watched stock moved more than the alert threshold (5%)
 * and displays a non-blocking alert banner.
 */
public class WatchlistView extends VBox  implements GameObserver {

  private static final BigDecimal ALERT_THRESHOLD = new BigDecimal("5.0");

  private static final String STYLE_BACKGROUND = "-fx-background-color: #0D1117;";
  private static final String STYLE_HEADER = "-fx-background-color: #161B22;"
      + " -fx-border-color: #21262D; -fx-border-width: 0 0 1 0;";
  private static final String STYLE_ROW = "-fx-border-color: #21262D;"
      + " -fx-border-width: 0 0 1 0;";
  private static final String STYLE_LABEL = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: monospace; -fx-font-size: 10px;";
  private static final String STYLE_VALUE = "-fx-text-fill: #E6EDF3;"
      + " -fx-font-family: monospace; -fx-font-size: 11px;";
  private static final String STYLE_ALERT = "-fx-background-color: #2D2A00;"
      + " -fx-border-color: #F8CC1B; -fx-border-width: 1;"
      + " -fx-padding: 8 12 8 12;";

  private final ExchangeController exchangeController;

  private final List<String> watchedSymbols = new ArrayList<>();
  private final Map<String, BigDecimal> priceSnapshot = new HashMap<>();

  private final VBox stockRows = new VBox();
  private final Label alertBanner = new Label();

  /**
   * Constructs the watchlist view and registers as a GameObserver.
   *
   * @param exchangeController the exchange controller
   */
  public WatchlistView(ExchangeController exchangeController) {
    this.exchangeController = exchangeController;
    buildLayout();
    exchangeController.getExchange().addObserver(this);
  }

  private void buildLayout() {
    setStyle(STYLE_BACKGROUND);
    setSpacing(0);
    setFillWidth(true);

    Label header = new Label("Watchlist");
    header.setStyle("-fx-text-fill: #8B949E;"
        + "-fx-font-family: monospace;"
        + "-fx-font-size: 9px;"
        + "-fx-font-weight: bold;");

    HBox headerBox = new HBox(header);
    headerBox.setPadding(new Insets(6, 12, 6, 12));
    headerBox.setStyle(STYLE_HEADER);

    alertBanner.setStyle("-fx-text-fill: #F8CC1B;"
        + "-fx-font-family: monospace;"
        + "-fx-font-size: 11px;"
        + "-fx-font-weight: bold;");
    alertBanner.setWrapText(true);

    VBox alertBox = new VBox(alertBanner);
    alertBox.setStyle(STYLE_ALERT);
    alertBox.setVisible(false);
    alertBox.setManaged(false);

    stockRows.setSpacing(0);
    stockRows.setFillWidth(true);

    Label empty = new Label("Nob stocks watched yet. \nClick * on a stock to add ");
    empty.setStyle(STYLE_LABEL);
    empty.setPadding(new Insets(16, 12, 16, 12));
    empty.setWrapText(true);
    stockRows.getChildren().add(empty);

    getChildren().addAll(headerBox, alertBox, stockRows);
  }

  /**
   * Adds a stock to the watchlist by symbol.
   * Records the current price as a snapshot for future comparison.
   *
   * @param symbol the stock symbol to watch
   */
  public void addToWatchList(String symbol) {
    if (!watchedSymbols.contains(symbol)) {
      watchedSymbols.add(symbol);
      Stock stock = exchangeController.getExchange().getStock(symbol);
      if (stock != null) {
        priceSnapshot.put(symbol, stock.getSalesPrice());
      }
      refreshRows();
    }
  }

  /**
   * Removes a stock from the watchlist.
   *
   * @param symbol the stock symbol to remove
   */
  public void removeFromWatchList(String symbol) {
    watchedSymbols.remove(symbol);
    priceSnapshot.remove(symbol);
    refreshRows();
  }

  /**
   * Returns whether the given symbol is currently on the watchlist.
   *
   * @param symbol the stock symbol to check
   * @return true if watched
   */
  public boolean isWatched(String symbol) {
    return watchedSymbols.contains(symbol);
  }

  /**
   * Called by Exchange on every game event.
   * On WEEK_ADVANCED, checks all watched stocks for price alerts
   *
   * @param gameEvent the event that was triggered
   */
  @Override
  public void onGameEvent(GameEvent gameEvent) {
    if (gameEvent == GameEvent.WEEK_ADVANCED) {
      checkAlerts();
      refreshRows();
    }
  }

  private void checkAlerts() {
    List<String> triggered = new ArrayList<>();

    for (String symbol : watchedSymbols) {
      Stock stock = exchangeController.getExchange().getStock(symbol);
      if (stock == null) {
        continue;
      }

      BigDecimal oldPrice = priceSnapshot.getOrDefault(symbol, stock.getSalesPrice());
      BigDecimal newPrice = stock.getSalesPrice();

      if (oldPrice.compareTo(BigDecimal.ZERO) == 0) {
        continue;
      }

      BigDecimal changePercent = newPrice.subtract(oldPrice)
          .divide(oldPrice, 4, RoundingMode.HALF_UP)
          .multiply(BigDecimal.valueOf(100))
          .abs();

      if (changePercent.compareTo(ALERT_THRESHOLD) >= 0) {
        BigDecimal change = stock.getLatestPriceChange();
        boolean up = change.compareTo(BigDecimal.ZERO) > 0;
        triggered.add(symbol + " " + (up ? "↑" : "↓")
            + changePercent.setScale(1, RoundingMode.HALF_UP) + "%");
      }
      priceSnapshot.put(symbol, newPrice);
    }

    VBox alertBox = (VBox) getChildren().get(1);
    if (triggered.isEmpty()) {
      alertBox.setVisible(false);
      alertBox.setManaged(false);
    } else {
      alertBanner.setText("⚠ Price alert: " + String.join(", ", triggered));
      alertBox.setVisible(true);
      alertBox.setManaged(true);
    }
  }

  private void refreshRows() {
    stockRows.getChildren().clear();

    if (watchedSymbols.isEmpty()) {
      Label empty = new Label("No stocks watched yet.\nClick ★ on a stock to add it.");
      empty.setStyle(STYLE_LABEL);
      empty.setPadding(new Insets(16, 12, 16, 12));
      empty.setWrapText(true);
      stockRows.getChildren().add(empty);
      return;
    }

    for (String symbol : watchedSymbols) {
      Stock stock = exchangeController.getExchange().getStock(symbol);
      if (stock != null) {
        stockRows.getChildren().add(buildRow(stock));
      }
    }
  }

  private HBox buildRow(Stock stock) {
    BigDecimal change = stock.getLatestPriceChange();
    boolean up = change.compareTo(BigDecimal.ZERO) > 0;
    String changeColor = up ? "#3FB950" : "#F85149";

    BigDecimal oldPrice = priceSnapshot.getOrDefault(stock.getSymbol(), stock.getSalesPrice());
    BigDecimal percent = oldPrice.compareTo(BigDecimal.ZERO) == 0
        ? BigDecimal.ZERO
        : stock.getSalesPrice().subtract(oldPrice)
            .divide(oldPrice, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));

    Label symbolLabel = new Label(stock.getSymbol());
    symbolLabel.setStyle(
        "-fx-text-fill: #E6EDF3;"
            + "-fx-font-family: monospace;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 11px;");

    Label priceLabel = new Label(CurrencyUtil.formatNok(stock.getSalesPrice()));
    priceLabel.setStyle(STYLE_VALUE);

    Label percentLabel = new Label(
        (up ? "+" : "") + percent.setScale(1, RoundingMode.HALF_UP) + "%");
    percentLabel.setStyle(
        "-fx-text-fill: " + changeColor + ";"
            + "-fx-font-family: monospace;"
            + "-fx-font-size: 10px; -fx-font-weight: bold;");

    Button removeButton = new Button("x");
    removeButton.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-text-fill: #6E7681;"
            + "-fx-font-size: 11px;"
            + "-fx-cursor: hand;"
    );
    removeButton.setOnAction(e -> removeFromWatchList(stock.getSymbol()));

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox row = new HBox(8, symbolLabel, spacer, priceLabel, percentLabel, removeButton);
    row.setPadding(new Insets(7, 12, 7, 12));
    row.setAlignment(Pos.CENTER_LEFT);
    row.setStyle(STYLE_ROW);
    return row;
  }
}
