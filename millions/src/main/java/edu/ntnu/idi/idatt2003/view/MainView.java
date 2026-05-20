package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import java.math.BigDecimal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * The root layout for the main game screen.
 * Uses BorderPane with a top bar, bottom status bar, and placeholder
 * regions for the stock list (left), detail panel (centre), and
 * portfolio sidebar (right), which will be filled in subsequently.
 */
public class MainView extends BorderPane implements GameObserver {
  private final ExchangeController exchangeController;
  private final PlayerController playerController;

  //Button
  private Button advanceButton;

  private final Label weekLabel = new Label("WEEK 1");
  private final Label cashLabel = new Label("Cash: " + CurrencyUtil.formatNok(BigDecimal.ZERO));

  /**
   * Constructs the main screen and registers as a GameObserver.
   *
   * @param exchangeController the exchange controller
   * @param playerController the player controller
   */
  public MainView(ExchangeController exchangeController, PlayerController playerController) {
    this.exchangeController = exchangeController;
    this.playerController = playerController;
    StatusBarView statusBar = new StatusBarView(playerController, exchangeController);

    setStyle("-fx-background-color: #0D1117;");
    setTop(buildTopBar());
    setBottom(statusBar);

    exchangeController.getExchange().addObserver(this);
    exchangeController.getExchange().addObserver(statusBar);
  }

  private HBox buildTopBar() {
    HBox bar = new HBox(12);
    bar.setPadding(new Insets(10, 14, 10, 14));
    bar.setStyle(
        "-fx-background-color: #161B22;"
            + "-fx-border-color: #30363D;"
            + "-fx-border-width: 0 0 1 0;");
    bar.setAlignment(Pos.CENTER_LEFT);

    Label logo = new Label("MILLIONS");
    logo.setStyle(
        "-fx-font-family: monospace;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 15px;"
            + "-fx-text-fill: #58A6FF;");

    weekLabel.setStyle(
        "-fx-background-color: #1F3A5F;"
            + "-fx-text-fill: #79C0FF;"
            + "-fx-padding: 2 9 2 9;"
            + "-fx-background-radius: 12;"
            + "-fx-font-family: monospace;"
            + "-fx-font-size: 10px;"
            + "-fx-font-weight: bold;");

    cashLabel.setStyle(
        "-fx-text-fill: #8B949E;"
            + "-fx-font-size: 11px;"
            + "-fx-font-family: monospace;");

    advanceButton = new Button("ADVANCE TO WEEK 2");
    advanceButton.setStyle(
        "-fx-background-color: #1F6FEB;"
            + "-fx-text-fill: white;"
            + "-fx-font-family: monospace;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 11px;"
            + "-fx-background-radius: 6;"
            + "-fx-cursor: hand;");
    advanceButton.setOnAction(e -> exchangeController.advance());

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    bar.getChildren().addAll(logo, weekLabel, spacer, cashLabel, advanceButton);
    return bar;
  }

  /**
   * Called automatically by Exchange whenever a game event fires.
   * Updates the cash display, week label, and advance button text.
   *
   * @param event the event that was triggered
   */
  @Override
  public void onGameEvent(GameEvent event) {
    switch (event) {
      case WEEK_ADVANCED -> refreshWeekDisplay();
      case PURCHASE_MADE, SALE_MADE -> refreshCashLabel();
      default -> {}
    }
  }

  private void refreshCashLabel() {
    BigDecimal money = playerController.getPlayer().getCurrentMoney();
    cashLabel.setText("Cash: kr" + CurrencyUtil.formatNok(money));
  }

  private void refreshWeekDisplay() {
    int week = exchangeController.getExchange().getWeek();
    weekLabel.setText("WEEK: " + week);
    advanceButton.setText("ADVANCE TO WEEK " + (week + 1));
    refreshCashLabel();
  }

  /**
   * Attaches the stock list panel to the left region.
   *
   * @param stockListView the stock list view
   */
  public void setStockListView(Node stockListView) {
    setLeft(stockListView);
  }

  /**
   * Attaches the portfolio sidebar to the right region.
   *
   * @param portfolioView the portfolio view
   */
  public void setPortfolioView(Node portfolioView) {
    setRight(portfolioView);
  }

  /**
   * Attaches the centre detail panel.
   *
   * @param stockDetailView the detail view
   */
  public void setStockDetailView(Node stockDetailView) {
    setCenter(stockDetailView);
  }
}
