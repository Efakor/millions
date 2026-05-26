package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Panel displaying detailed information about a selected stock.
 * Shows current price, highest price, lowest price, latest price change
 * and a line chart of the full price history built using Java streams.
 * Also provides buy and watchlist buttons for the selected stock.
 *
 */


public class StockDetailPanel extends VBox {
  private static final String STYLE_PANEL = "-fx-background-color: #161B22;"
      + " -fx-border-color: #30363D; -fx-border-width: 1;";
  private static final String STYLE_TITLE = "-fx-text-fill: #58A6FF;"
      + " -fx-font-size: 22px; -fx-font-weight: bold;";
  private static final String STYLE_SUBTITLE = "-fx-text-fill: #8B949E;"
      + " -fx-font-size: 12px;";
  private static final String STYLE_LABEL = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: monospace; -fx-font-size: 11px;";
  private static final String STYLE_VALUE = "-fx-text-fill: #E6EDF3;"
      + " -fx-font-family: monospace; -fx-font-size: 12px;";
  private static final String STYLE_TOTAL = "-fx-text-fill: #3FB950;"
      + " -fx-font-family: monospace; -fx-font-size: 15px; -fx-font-weight: bold;";
  private static final String STYLE_BUTTON_OK = "-fx-background-color: #3FB950;"
      + " -fx-text-fill: white; -fx-font-family: monospace;"
      + " -fx-font-weight: bold; -fx-padding: 10 20;";


  private static final String COLOR_RED = "#F85149";
  private static final String COLOR_GREEN = "#3FB950";


  private Stock currentStock;
  private LineChart<Number, Number> lineChart;
  private final ExchangeController exchangeController;
  private final PlayerController playerController;
  private final WatchlistView watchlistView;
  // Controlls
  private final Button buyButton = new Button();
  private final Button watchlistButton = new Button("+WATCHLIST");

  // UI Labels
  private final Label symbolLabel = new Label();
  private final Label subtitleLabel = new Label();

  // Grid value labels
  private final Label latestChangeLabel = new Label();
  private final Label lowestPriceLabel = new Label();
  private final Label highestPriceLabel = new Label();
  private final Label currentPriceLabel = new Label();

  private GridPane gridPane;

  /**
   * Constructs the stock detail panel with buy and watchlist functionality.
   *
   * @param exchangeController the exchange controller handling transactions
   * @param playerController the player controller providing the player state
   * @param watchlistView the watchlist view for saving the watched stock
   */


  public StockDetailPanel(ExchangeController exchangeController,
                          PlayerController playerController,
                          WatchlistView watchlistView) {
    this.exchangeController = exchangeController;
    this.playerController = playerController;
    this.watchlistView = watchlistView;
    setSpacing(15);
    setPadding(new Insets(15));
    buyButton.setOnAction(e -> {
      if (currentStock != null) {
        Stage currentStage = (Stage) getScene().getWindow();
        BuyDialog.showAndWait(currentStock, exchangeController, playerController);
      }
    });
    watchlistButton.setOnAction(e -> {
      if (currentStock != null) {
        String symbol = currentStock.getSymbol();
        if (watchlistView.isWatched(symbol)) {
          watchlistView.removeFromWatchList(currentStock.getSymbol());
          watchlistButton.setText("+WATCHLIST");
        } else {
          watchlistView.addToWatchList(currentStock.getSymbol());
          watchlistButton.setText("★ WATCHLIST");
        }
        updateWatchlistButton(symbol);
      }
    });
    buildUi();

  }

  /**
   * Builds the panel UI including the header stats grid and button row.
   */

  private void buildUi() {


    gridPane = new GridPane();
    gridPane.setHgap(20);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(15));
    gridPane.setStyle(STYLE_PANEL);


    addRow(gridPane, 0, "Current Price", currentPriceLabel, STYLE_TOTAL);
    addRow(gridPane, 1, "Highest Price", highestPriceLabel, STYLE_VALUE);
    addRow(gridPane, 2, "Lowest Price", lowestPriceLabel, STYLE_VALUE);
    addRow(gridPane, 3, "Latest Change", latestChangeLabel, STYLE_VALUE);

    buyButton.setMaxWidth(Double.MAX_VALUE);
    buyButton.setStyle(STYLE_BUTTON_OK);
    HBox.setHgrow(buyButton, Priority.ALWAYS);
    HBox buttonBox = new HBox(12, buyButton, watchlistButton);
    buttonBox.setAlignment(Pos.CENTER_RIGHT);

    symbolLabel.setStyle(STYLE_TITLE);
    subtitleLabel.setStyle(STYLE_SUBTITLE);
    VBox headerBox = new VBox(2, symbolLabel, subtitleLabel);

    getChildren().addAll(headerBox, gridPane, buttonBox);

  }

  /**
   * Adds a single labelled row to the stats grid with a spacer between the labeled value.
   *
   * @param gridPane the GridPane to add the row to
   * @param row   the row index
   * @param labelText the left column label text
   * @param valuelabel the label node for the right column
   * @param valueStyle the CSS style to apply to the value label
   */

  private void addRow(GridPane gridPane,
                      int row, String labelText,
                      Label valuelabel,
                      String valueStyle) {
    Label label = new Label(labelText);
    label.setStyle(STYLE_LABEL);

    valuelabel.setStyle(valueStyle);
    Region spacer = new Region();
    GridPane.setHgrow(spacer, Priority.ALWAYS);
    gridPane.add(label, 0, row);
    gridPane.add(spacer, 1, row);
    gridPane.add(valuelabel, 2, row);
  }
  /**
   * Updates the watchlist button text based on whether the given
   * stock symbol is currently on the watchlist.
   *
   * @param symbol the stock symbol to check
   */

  private void updateWatchlistButton(String symbol) {
    if (watchlistView.isWatched(symbol)) {
      watchlistButton.setText("+WATCHLIST");
    } else {
      watchlistButton.setText("★WATCHLIST");
    }
  }
  /**
   * Displays and updates all labels, applies colour coding based on price direction
   * and rebuilds the price history chart.
   * Hides the panel if the stock is null.
   *
   * @param stock the stock to display, or null to hide the panel
   */

  public void showStock(Stock stock) {
    this.currentStock = stock;
    if (stock == null) {
      setVisible(false);
      setManaged(false);
      return;


    }
    // Add these in showStock()
    highestPriceLabel.setStyle(STYLE_VALUE + "; -fx-text-fill: #E6EDF3;");
    lowestPriceLabel.setStyle(STYLE_VALUE + "; -fx-text-fill: #E6EDF3;");


    symbolLabel.setText(stock.getSymbol());
    subtitleLabel.setText(stock.getCompany());
    buyButton.setText("BUY " + stock.getSymbol());
    currentPriceLabel.setText(CurrencyUtil.formatNok(stock.getSalesPrice()));

    highestPriceLabel.setText(CurrencyUtil.formatNok(stock.getHighestPrice()));
    lowestPriceLabel.setText(CurrencyUtil.formatNok(stock.getLowestPrice()));
    latestChangeLabel.setText(CurrencyUtil.formatNok(stock.getLatestPriceChange()));

    // Layout
    BigDecimal change = stock.getLatestPriceChange();
    boolean isPositive = change.compareTo(BigDecimal.ZERO) > 0;
    String changeText = (isPositive ? "+" : "")
        + change.setScale(2, BigDecimal.ROUND_HALF_UP) + "%";

    latestChangeLabel.setText(changeText);
    latestChangeLabel.setStyle(STYLE_VALUE
        + ";-fx-text-fill:"
        + (isPositive ? COLOR_GREEN : COLOR_RED));
    currentPriceLabel.setStyle(STYLE_TOTAL
        + ";-fx-text-fill:"
        + (isPositive ? COLOR_GREEN : COLOR_RED));
    updateWatchlistButton(stock.getSymbol());
    setVisible(true);
    setManaged(true);

    buildChart(stock, isPositive);
  }
  /**
   * Displays the given stock in the detail panel.
   * Updates all labels, applies colour coding based on price direction
   * and rebuilds the price history chart.
   * Hides the panel if the stock is null.
   *
   * @param stock the stock to display, or null to hide the panel
   */

  private void buildChart(Stock stock, boolean isPositive) {
    if (lineChart != null) {
      getChildren().remove(lineChart);
    }
    NumberAxis xaxis = new NumberAxis();
    NumberAxis yaxis = new NumberAxis();
    xaxis.setOpacity(1);
    xaxis.setLabel("Week");

    yaxis.setLabel("Price");
    yaxis.setOpacity(1);

    xaxis.setTickLabelFill(javafx.scene.paint.Color.web("#8B949E"));
    yaxis.setTickLabelFill(javafx.scene.paint.Color.web("#8B949E"));
    xaxis.setStyle("-fx-tick-label-fill: #8B949E; -fx-font-family: monospace; -fx-font-size: 9px;");
    yaxis.setStyle("-fx-tick-label-fill: #8B949E; -fx-font-family: monospace; -fx-font-size: 9px;");

    LineChart<Number, Number> graph = new LineChart<Number, Number>(xaxis, yaxis);
    graph.setPrefHeight(200);
    graph.setLegendVisible(false);
    graph.setCreateSymbols(false);
    graph.setHorizontalGridLinesVisible(false);
    graph.setVerticalGridLinesVisible(false);
    graph.setAlternativeRowFillVisible(false);
    graph.setAlternativeColumnFillVisible(false);

    // Define a series
    XYChart.Series<Number, Number> series = new XYChart.Series<>();

    List<BigDecimal> prices = stock.getHistoricalPrices();
    IntStream.range(0, prices.size()).forEach(i ->
        series.getData().add(new XYChart.Data<>(i + 1, prices.get(i).doubleValue())
        ));
    graph.getData().add(series);
    // Used claude AI  to get the background black for aesthetic reasons
    javafx.application.Platform.runLater(() -> {
      graph.lookup(".chart-plot-background")
          .setStyle("-fx-background-color: #0D1117;");
      graph.lookup(".chart-content")
          .setStyle("-fx-background-color: #0D1117;");
      graph.lookupAll(".chart-series-line").forEach(node ->
          node.setStyle("-fx-stroke: #3FB950; -fx-stroke-width: 2px;"));
      graph.lookupAll(".default-color0.chart-symbol").forEach(node ->
          node.setStyle("-fx-background-color: transparent;"));
    });

    String trendColor = isPositive ? COLOR_GREEN : COLOR_RED;
    series.getNode().setStyle("-fx-stroke: " + trendColor + "; -fx-stroke-width: 2.5px;");
    lineChart = graph;
    getChildren().add(1, lineChart);
  }


}


