package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Sidebar showing the player's current holdings.
 * Registers as a GameObserver and refreshes automatically on every event.
 */
public class PortfolioView extends BorderPane implements GameObserver {
  private final PlayerController playerController;
  private final ExchangeController exchangeController;

  private final VBox holdingsList = new VBox();
  private final Label totalLabel = new Label(CurrencyUtil.formatNok(BigDecimal.ZERO));

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
    sellAllButton.setOnAction(e ->SellAllDialog.show(exchangeController,playerController,(Stage) getScene().getWindow()));

    VBox box = new VBox(6, totalLbl, totalLabel, sellAllButton);
    box.setPadding(new Insets(10, 12, 10, 12));
    box.setStyle("-fx-background-color: #161B22;"
        + "-fx-border-color: #21262D;"
        + "-fx-border-width: 1 0 0 0;");
    return box;
  }

  @Override
  public void onGameEvent(GameEvent event) {
    refresh();
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
    for (Share share : shares) {
      Stock stock = exchangeController.getExchange().getStock(share.stock().getSymbol());
      if (stock == null) continue;

      BigDecimal currentPrice = stock.getSalesPrice();
      BigDecimal currentValue = currentPrice.multiply(share.quantity());
      BigDecimal costBasis = share.purchasePrice().multiply(share.quantity());
      BigDecimal pnl = currentValue.subtract(costBasis);
      BigDecimal pnlPct = costBasis.compareTo(BigDecimal.ZERO) == 0
          ? BigDecimal.ZERO
          : pnl.divide(costBasis, 4, RoundingMode.HALF_UP)
          .multiply(BigDecimal.valueOf(100));

      holdingsList.getChildren().add(buildHoldingRow(share, stock, currentValue, pnl, pnlPct));
      portfolioTotal = portfolioTotal.add(currentValue);
    }

    totalLabel.setText(CurrencyUtil.formatNok(portfolioTotal));
  }

  private HBox buildHoldingRow(Share share, Stock stock,
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
        + " sh · avg " + CurrencyUtil.formatNok(share.purchasePrice())
    );
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

  VBox left = new VBox(2, symbolLabel, metaLabel, sellButton);

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

  Region spacer1= new Region();
  HBox.setHgrow(spacer1, Priority.ALWAYS);

  Region spacer2= new Region();
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
