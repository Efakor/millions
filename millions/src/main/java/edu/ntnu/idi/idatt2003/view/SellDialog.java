package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.model.calculator.SaleCalculator;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import java.math.BigDecimal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Modal dialog shown when the player clicks Sell on a holding.
 * Displays the full SaleCalculator breakdown before confirming.
 */
public class SellDialog {
  private static final String STYLE_BACKGROUND     = "-fx-background-color: #0D1117;";
  private static final String STYLE_PANEL  = "-fx-background-color: #161B22;"
      + " -fx-border-color: #30363D; -fx-border-width: 1;";
  private static final String STYLE_LABEL  = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: monospace; -fx-font-size: 11px;";
  private static final String STYLE_VALUE  = "-fx-text-fill: #E6EDF3;"
      + " -fx-font-family: monospace; -fx-font-size: 12px;";
  private static final String STYLE_TOTAL  = "-fx-text-fill: #3FB950;"
      + " -fx-font-family: monospace; -fx-font-size: 15px; -fx-font-weight: bold;";
  private static final String STYLE_BUTTON_OK = "-fx-background-color: #3FB950;"
      + " -fx-text-fill: white; -fx-font-family: monospace;"
      + " -fx-font-weight: bold; -fx-padding: 10 20;";
  private static final String STYLE_BUTTON_OFF = "-fx-background-color: #30363D;"
      + " -fx-text-fill: #6E7681; -fx-font-family: monospace;"
      + " -fx-font-weight: bold; -fx-padding: 10 20;";

  private final Share share;
  private final ExchangeController exchangeController;
  private final Stage stage = new Stage();

  private SellDialog(Share share, ExchangeController exchangeController,
                     PlayerController playerController) {
    this.share = share;
    this.exchangeController = exchangeController;
    buildUi();
  }

  /**
   * Shows the sell dialog and waits for the user to confirm or cancel.
   *
   * @param share the share to sell
   * @param exchangeController the exchange controller
   * @param playerController the player controller
   */
  public static void showAndWait(Share share, ExchangeController exchangeController,
                                 PlayerController playerController) {
    new SellDialog(share, exchangeController, playerController).stage.showAndWait();
  }

  private void buildUi() {
    Stock stock = exchangeController.getExchange().getStock(share.stock().getSymbol());
    SaleCalculator calc = new SaleCalculator(share);

    BigDecimal gross = calc.calculateGross();
    BigDecimal commission = calc.calculateCommission();
    BigDecimal total = calc.calculateTotal();
    BigDecimal tax = calc.calculateTax();
    BigDecimal costBasis = share.purchasePrice().multiply(share.quantity());
    BigDecimal pnl = total.subtract(costBasis);

    Label title = new Label("SELL " + share.stock().getSymbol());
    title.setStyle("-fx-text-fill: #E6EDF3;"
        + "-fx-font-family: monospace;"
        + "-fx-font-size: 16px; -fx-font-weight: bold;");

    String priceInfo = share.quantity().toPlainString()
        + " shares · bought at "
        + CurrencyUtil.formatNok(share.purchasePrice());
    Label subtitle = new Label(priceInfo);
    subtitle.setStyle(STYLE_LABEL);

    GridPane breakdown = buildBreakdown(stock, gross, commission, tax, total);

    boolean profit = pnl.compareTo(BigDecimal.ZERO) >= 0;
    String pnlColor = profit ? "#3FB950" : "#F85149";
    String pnlSign = profit ? "+" : "-";
    Label pnlLabel = new Label((profit ? "PROFIT: " : "LOSS: ")
        + pnlSign + CurrencyUtil.formatNok(pnl.abs())
        + " vs purchase");
    pnlLabel.setStyle("-fx-text-fill: " + pnlColor + ";"
        + "-fx-font-family: monospace;"
        + "-fx-font-weight: bold;"
        + "-fx-font-size: 12px;");

    Button cancelButton = new Button("CANCEL");
    cancelButton.setStyle(STYLE_BUTTON_OFF);
    cancelButton.setOnAction(e -> stage.close());

    Button confirmButton = new Button("CONFIRM SELL");
    confirmButton.setStyle(STYLE_BUTTON_OK);
    confirmButton.setDefaultButton(true);
    confirmButton.setOnAction(e -> {
      exchangeController.sell(share);
      stage.close();
    });

    HBox buttons = new HBox(10, cancelButton, confirmButton);
    buttons.setAlignment(Pos.CENTER_RIGHT);

    VBox root = new VBox(14, title, subtitle, breakdown, pnlLabel, buttons);
    root.setPadding(new Insets(20));
    root.setStyle(STYLE_BACKGROUND);

    stage.setScene(new Scene(root, 380, 380));
    stage.setTitle("Sell" + share.stock().getSymbol());
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setResizable(false);
  }

  private GridPane buildBreakdown(Stock stock, BigDecimal gross,
                                  BigDecimal commission, BigDecimal tax, BigDecimal total) {
    GridPane grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(8);
    grid.setPadding(new Insets(14));
    grid.setStyle(STYLE_PANEL);

    addRow(grid, 0, "Sales price", CurrencyUtil.formatNok(stock.getSalesPrice()), STYLE_VALUE);
    addRow(grid, 1, "Gross", CurrencyUtil.formatNok(gross), STYLE_VALUE);
    addRow(grid, 2, "Commission (1%)", CurrencyUtil.formatNok(commission), STYLE_VALUE);
    addRow(grid, 3, "Tax on gain (30%)", CurrencyUtil.formatNok(tax), STYLE_VALUE);
    addRow(grid, 4, "Net proceeds", CurrencyUtil.formatNok(total), STYLE_TOTAL);
    return grid;
  }

  private void addRow(GridPane grid, int row, String labelText, String value, String valueStyle) {
    Label label = new Label(labelText);
    label.setStyle(STYLE_LABEL);

    Label val = new Label(value);
    val.setStyle(valueStyle);

    Region spacer = new Region();
    GridPane.setHgrow(spacer, Priority.ALWAYS);

    grid.add(label, 0, row);
    grid.add(spacer, 1, row);
    grid.add(val, 2, row);
  }
}
