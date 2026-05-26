package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Sidebar showing the player's current holdings.
 * Registers as a GameObserver and refreshes automatically on every event.
 */
public class PortfolioView extends BorderPane implements GameObserver {
  private final PlayerController playerController;
  private final ExchangeController exchangeController;

  private final VBox holdingsList = new VBox();
  private final Label totalLabel = new Label(CurrencyUtil.formatNok(BigDecimal.ZERO));
  private Runnable sellAllRequest;

  /**
   * Constructs the portfolio sidebar.
   *
   * @param playerController the player controller
   * @param exchangeController the exchange controller
   */
  public PortfolioView(PlayerController playerController, ExchangeController exchangeController) {
    this.playerController = playerController;
    this.exchangeController = exchangeController;
    buildLayout();
    exchangeController.getExchange().addObserver(this);
  }

  /**
   * Builds the full panel layout including the header
   * and total value footer with the sell all button.
   */

  private void buildLayout() {
    setStyle("-fx-background-color: #0D1117;");

    Label header = new Label("PORTFOLIO");
    header.setStyle(
        "-fx-text-fill: #8B949E;"
            + "-fx-font-family: monospace;"
            + "-fx-font-size: 9px;"
            + "-fx-font-weight: bold;"
    );

    HBox headerBox = new HBox(header);
    headerBox.setPadding(new Insets(6, 12, 6, 12));
    headerBox.setStyle("-fx-background-color: #161B22;"
        + "-fx-border-color: #21262D;"
        + "-fx-border-width: 0 0 1 0;");

    holdingsList.setSpacing(0);
    holdingsList.setFillWidth(true);

    ScrollPane scrollPane = new ScrollPane(holdingsList);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #0D1117;");

    VBox totalBox = buildTotalBox();

    setTop(headerBox);
    setCenter(scrollPane);
    setBottom(totalBox);
  }

  /**
   * Sets the callback, when sell all button is confirmed.
   *
   * @param sellAllRequest the runnable call when sell all is triggered
   */

  public void setSellAllRequest(Runnable sellAllRequest) {
    this.sellAllRequest = sellAllRequest;

  }

  /**
   * Builds the bottom total value showing portfolio value and sell all button.
   *
   * @return a VBox containing the total label and sell all button
   */

  private VBox buildTotalBox() {
    Label totalLbl = new Label("TOTAL VALUE");
    totalLbl.setStyle(
        "-fx-text-fill: #8B949E;"
        + "-fx-font-family: monospace;"
        + "-fx-font-size: 9px;"
    );

    totalLabel.setStyle(
        "-fx-text-fill: #E6EDF3;"
            + "-fx-font-family: monospace;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 13px;"
    );

    Button sellAllButton = new Button("SELL ALL & EXIT");
    sellAllButton.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-text-fill: #F85149;"
            + "-fx-border-color: #F85149;"
            + "-fx-border-width: 1;"
            + "-fx-font-family: monospace;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 11px;"
            + "-fx-cursor: hand;"
    );
    sellAllButton.setMaxWidth(Double.MAX_VALUE);
    sellAllButton.setOnAction(e -> {
      // confirmation dialog
      javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
          javafx.scene.control.Alert.AlertType.CONFIRMATION);
      confirm.setTitle("Sell All & Exit");
      confirm.setHeaderText("Are you sure you want to sell everything and quit?");
      confirm.setContentText(
          "Estimated proceeds: "
              + CurrencyUtil.formatNok(
              playerController.getPlayer().getPortfolio().getNetWorth()));
      confirm.getDialogPane().setStyle("-fx-background-color: #161B22;");

      confirm.showAndWait().ifPresent(response -> {
        if (response == javafx.scene.control.ButtonType.OK) {
          playerController.sellAll();
          showFinalSummary();
        }
      });
    });

    VBox box = new VBox(6, totalLbl, totalLabel, sellAllButton);
    box.setPadding(new Insets(10, 12, 10, 12));
    box.setStyle("-fx-background-color: #161B22;"
        + "-fx-border-color: #21262D;"
        + "-fx-border-width: 1 0 0 0;");
    return box;
  }

  /**
   *Called by the exchange when a game event is fired.
   * Plans the UI refresh on the JavaFX application thread.
   *
   * @param event the event that was triggered
   */

  @Override
  public void onGameEvent(GameEvent event) {
    Platform.runLater(this::refresh);
  }

  private void refresh() {
    holdingsList.getChildren().clear();

    List<Share> shares = playerController.getPlayer()
        .getPortfolio()
        .getAllShares();

    if (shares.isEmpty()) {
      Label empty = new Label("No holdings yet");
      empty.setStyle(
          "-fx-text-fill: #8B949E;"
              + "-fx-font-family: monospace;"
              + "-fx-font-size: 11px;"
      );
      empty.setPadding(new Insets(20, 12, 20, 12));
      holdingsList.getChildren().add(empty);
      totalLabel.setText(CurrencyUtil.formatNok(BigDecimal.ZERO));
      return;
    }

    BigDecimal portfolioTotal = BigDecimal.ZERO;
    // group shares by symbol
    java.util.Map<String, List<Share>> grouped = shares.stream()
        .collect(java.util.stream.Collectors.groupingBy(
            s -> s.stock().getSymbol()));

    for (java.util.Map.Entry<String, List<Share>> entry : grouped.entrySet()) {
      String symbol         = entry.getKey();
      List<Share> group     = entry.getValue();
      Stock stock           = exchangeController.getExchange().getStock(symbol);
      if (stock == null) {
        continue;
      }

      totalLabel.setText(CurrencyUtil.formatNok(portfolioTotal));

      // total quantity
      BigDecimal totalQty = group.stream()
          .map(Share::quantity)
          .reduce(BigDecimal.ZERO, BigDecimal::add);

      // weighted average purchase price
      BigDecimal totalCost = group.stream()
          .map(s -> s.purchasePrice().multiply(s.quantity()))
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      BigDecimal avgPrice = totalQty.compareTo(BigDecimal.ZERO) == 0
          ? BigDecimal.ZERO
          : totalCost.divide(totalQty, 2, RoundingMode.HALF_UP);

      BigDecimal currentValue = stock.getSalesPrice().multiply(totalQty);
      BigDecimal pnl          = currentValue.subtract(totalCost);
      BigDecimal pnlPct       = totalCost.compareTo(BigDecimal.ZERO) == 0
          ? BigDecimal.ZERO
          : pnl.divide(totalCost, 4, RoundingMode.HALF_UP)
          .multiply(BigDecimal.valueOf(100));

      // use first share as representative — sell button will sell all shares of this symbol
      Share representative = group.getFirst();
      holdingsList.getChildren().add(
          buildHoldingRow(
              representative, stock, totalQty, avgPrice, currentValue, pnl, pnlPct));

      portfolioTotal = portfolioTotal.add(currentValue);
    }
  }

  /**
   * Refreshes the holdings lists with current portfolio data.
   */

  private void showFinalSummary() {
    javafx.scene.control.Alert summary = new javafx.scene.control.Alert(
        javafx.scene.control.Alert.AlertType.INFORMATION);
    summary.setTitle("Game Over");
    summary.setHeaderText("Final Summary");

    BigDecimal netWorth    = playerController.getPlayer().getNetWorth();
    BigDecimal startingCap = playerController.getPlayer().getStartingMoney();
    BigDecimal pnl         = netWorth.subtract(startingCap);
    String status          = playerController.getPlayer().getStatus();
    boolean profit         = pnl.compareTo(BigDecimal.ZERO) >= 0;

    summary.setContentText(
        "Net worth:    " + CurrencyUtil.formatNok(netWorth) + "\n"
            + "P&L:          " + (profit ? "+" : "") + CurrencyUtil.formatNok(pnl) + "\n"
            + "Status:       " + status + "\n"
            + "Weeks played: "
            + playerController.getPlayer().getTransactionArchive().countDistinctWeeks()
    );

    summary.showAndWait();
    javafx.application.Platform.exit();
  }

  private HBox buildHoldingRow(Share share, Stock stock, BigDecimal totalQty, BigDecimal avgPrice,
                               BigDecimal currentValue, BigDecimal pnl, BigDecimal pnlPct) {
    Label symbolLabel = new Label(share.stock().getSymbol());
    symbolLabel.setStyle(
        "-fx-text-fill: #E6EDF3;"
        + "-fx-font-family: monospace;"
        + "-fx-font-size: 11px;"
        + "-fx-font-weight: bold"
    );

    Label metaLabel = new Label(
        share.quantity().toPlainString()
        + " sh · avg " + CurrencyUtil.formatNok(avgPrice));
    metaLabel.setStyle(
        "-fx-text-fill: #8B949E;"
            + "-fx-font-family: monospace;"
            + "-fx-font-size: 10px;"
    );

    Button sellButton = new Button("SELL");
    sellButton.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-text-fill: #F85149;"
            + "-fx-border-color: #F85149;"
            + "-fx-border-width: 1;"
            + "-fx-font-family: monospace;"
            + "-fx-font-size: 10px;"
            + "-fx-cursor: hand;"
    );
    sellButton.setOnAction(e -> SellDialog.showAndWait(
        share, exchangeController, playerController));


    boolean profit = pnl.compareTo(BigDecimal.ZERO) >= 0;
    String pnlColor = profit ? "#3FB950" : "#F85149";
    String pnlSign = profit ? "+" : "-";

    Label pnlLabel = new Label(
        pnlSign + CurrencyUtil.formatNok(pnl.abs())
     );
    pnlLabel.setStyle(
         "-fx-text-fill: " + pnlColor + ";"
          + "-fx-font-family: monospace;"
          + "-fx-font-weight: bold;"
          + "-fx-font-size: 11px;"
    );

    Label valLabel = new Label(CurrencyUtil.formatNok(currentValue));
    valLabel.setStyle(
        "-fx-text-fill: #8B949E;"
          + "-fx-font-family: monospace;"
          + "-fx-font-size: 10px;"
    );

    Label pctLabel = new Label(
        pnlSign + pnlPct.setScale(1, RoundingMode.HALF_UP) + "%");
    pctLabel.setStyle(
        "-fx-text-fill: " + pnlColor + ";"
        + "-fx-font-family: monospace;"
            + "-fx-font-size: 10px;"
    );
    VBox right = new VBox(2, pnlLabel, valLabel, pctLabel);
    right.setAlignment(Pos.CENTER_RIGHT);
    VBox left = new VBox(2, symbolLabel, metaLabel, sellButton);

    Region spacer1 = new Region();
    HBox.setHgrow(spacer1, Priority.ALWAYS);

    Region spacer2 = new Region();
    HBox.setHgrow(spacer2, Priority.ALWAYS);
    HBox row = new HBox(spacer1, left, spacer2, right);
    row.setPadding(new Insets(8, 12, 8, 12));
    row.setStyle(
        "-fx-border-color: #21262D;"
          + "-fx-border-width: 0 0 1 0;");
    row.setAlignment(Pos.CENTER_LEFT);
    return row;
  }
}
