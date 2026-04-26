package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.model.Transaction;
import edu.ntnu.idi.idatt2003.model.calculator.PurchaseCalculator;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
    import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Optional;


public class BuyDialog {

  private static final String STYLE_DARK_BACKGROUND  = "-fx-background-color: #0D1117;";
  private static final String STYLE_PANEL    = "-fx-background-color: #161B22;"
      + " -fx-border-color: #30363D; -fx-border-width: 1;";
  private static final String STYLE_LABEL    = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: 'JetBrains Mono', monospace; -fx-font-size: 11;";
  private static final String STYLE_VALUE    = "-fx-text-fill: #E6EDF3;"
      + " -fx-font-family: 'JetBrains Mono', monospace; -fx-font-size: 13;";
  private static final String STYLE_TOTAL    = "-fx-text-fill: #2EA043;"
      + " -fx-font-family: 'JetBrains Mono', monospace;"
      + " -fx-font-size: 16; -fx-font-weight: bold;";
  private static final String STYLE_WARNING     = "-fx-text-fill: #F85149;"
      + " -fx-font-family: 'JetBrains Mono', monospace; -fx-font-size: 11;";
  private static final String STYLE_BUTTON_OK   = "-fx-background-color: #2EA043;"
      + " -fx-text-fill: white; -fx-font-family: 'JetBrains Mono', monospace;"
      + " -fx-font-weight: bold; -fx-padding: 10 20;";
  private static final String STYLE_BUTTON_OFF  = "-fx-background-color: #30363D;"
      + " -fx-text-fill: #6E7681; -fx-font-family: 'JetBrains Mono', monospace;"
      + " -fx-font-weight: bold; -fx-padding: 10 20;";

  private final Stock stock;
  private final ExchangeController exchangeController;
  private final PlayerController playerController;
  private final Stage stage = new Stage();

  private final TextField quantityField     = new TextField("1");
  private final Label grossLabel       = new Label("0.00");
  private final Label commissionLabel  = new Label("0.00");
  private final Label taxLabel         = new Label("0.00");
  private final Label totalLabel       = new Label("0.00");
  private final Label fundsWarning     = new Label();
  private final Button confirmButton      = new Button("CONFIRM PURCHASE");

  private Transaction result;

  private BuyDialog(Stock stock, ExchangeController exchange, PlayerController player) {
    this.stock = stock;
    this.exchangeController = exchange;
    this.playerController = player;
    buildUi();
    wireListeners();
    refreshPreview();
  }

  public static Optional<Transaction> showAndWait(
      Stock stock, ExchangeController exchange, PlayerController player) {
    BuyDialog buyDialog = new BuyDialog(stock, exchange, player);
    buyDialog.stage.showAndWait();
    return Optional.ofNullable(buyDialog.result);
  }

  private void buildUi() {
    Label title = new Label(
        "BUY " + stock.getSymbol() + " @ " + stock.getSalesPrice().toPlainString());
    title.setStyle("-fx-text-fill: #E6EDF3;"
        + " -fx-font-family: 'JetBrains Mono', monospace;"
        + " -fx-font-size: 16; -fx-font-weight: bold;");

    HBox quantityRow = buildQuantityRow();
    GridPane breakdown = buildBreakdown();

    fundsWarning.setStyle(STYLE_WARNING);
    fundsWarning.setVisible(false);
    fundsWarning.setManaged(false);

    Button cancelButton = new Button("CANCEL");
    cancelButton.setStyle(STYLE_BUTTON_OFF);
    cancelButton.setOnAction(e -> stage.close());

    confirmButton.setStyle(STYLE_BUTTON_OK);
    confirmButton.setDefaultButton(true);
    confirmButton.setOnAction(e -> onConfirm());

    HBox buttons = new HBox(10, cancelButton, confirmButton);
    buttons.setAlignment(Pos.CENTER_RIGHT);

    VBox root = new VBox(14, title, quantityRow, breakdown, fundsWarning, buttons);
    root.setPadding(new Insets(20));
    root.setStyle(STYLE_DARK_BACKGROUND);

    Scene scene = new Scene(root, 380, 360);
    stage.setScene(scene);
    stage.setTitle("Buy " + stock.getSymbol());
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setResizable(false);
  }

  private HBox buildQuantityRow() {
    Label quantityLabel = new Label("QUANTITY");
    quantityLabel.setStyle(STYLE_LABEL);

    Button minus = new Button("-");
    minus.setStyle(STYLE_BUTTON_OFF);
    minus.setOnAction(e -> bumpQuantity(BigDecimal.ONE.negate()));

    Button plus = new Button("+");
    plus.setStyle(STYLE_BUTTON_OFF);
    plus.setOnAction(e -> bumpQuantity(BigDecimal.ONE));

    quantityField.setStyle(STYLE_VALUE
        + " -fx-background-color: #0D1117;"
        + " -fx-border-color: #30363D; -fx-border-width: 1;"
        + " -fx-alignment: center; -fx-pref-width: 100;");

    HBox stepper = new HBox(6, minus, quantityField, plus);
    stepper.setAlignment(Pos.CENTER_LEFT);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Label suffix = new Label("shares");
    suffix.setStyle(STYLE_LABEL);

    HBox row = new HBox(12, quantityLabel, suffix, stepper, spacer);
    row.setAlignment(Pos.CENTER_LEFT);
    row.setPadding(new Insets(10));
    row.setStyle(STYLE_PANEL);
    return row;
  }

  private GridPane buildBreakdown() {
    GridPane grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(8);
    grid.setPadding(new Insets(14));
    grid.setStyle(STYLE_PANEL);

    addBreakdownRow(grid, 0, "Gross", grossLabel, STYLE_VALUE);
    addBreakdownRow(grid, 1, "Commission (0.5%)", commissionLabel, STYLE_VALUE);
    addBreakdownRow(grid, 2, "Tax", taxLabel, STYLE_VALUE);
    addBreakdownRow(grid, 3, "Total", totalLabel, STYLE_TOTAL);
    return grid;
  }

  private void addBreakdownRow(GridPane grid, int row,
                               String label, Label valueLabel, String valueStyle) {
    Label l = new Label(label);
    l.setStyle(STYLE_LABEL);
    valueLabel.setStyle(valueStyle);

    Region spacer = new Region();
    GridPane.setHgrow(spacer, Priority.ALWAYS);

    grid.add(l, 0, row);
    grid.add(spacer, 1, row);
    grid.add(valueLabel, 2, row);
  }

  private void bumpQuantity(BigDecimal delta) {
    BigDecimal current = parseQuantityOrZero();
    BigDecimal next = current.add(delta);
    if (next.compareTo(BigDecimal.ONE) < 0) {
      next = BigDecimal.ONE;
    }
    quantityField.setText(next.toPlainString());
  }

  private void wireListeners() {
    quantityField.textProperty().addListener(
        (observable, oldValue, newValue) -> refreshPreview());
  }

  private void refreshPreview() {
    BigDecimal quantity = parseQuantityOrZero();
    if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
      grossLabel.setText("-");
      commissionLabel.setText("-");
      taxLabel.setText("-");
      totalLabel.setText("-");
      disableConfirm("Quantity must be greater than 0");
      return;
    }

    // Reuse the same calculator the actual transaction will use so the
    // preview is guaranteed to match the real charge.
    Share previewShare = new Share(stock, quantity, stock.getSalesPrice());
    PurchaseCalculator calc = new PurchaseCalculator(previewShare);

    BigDecimal gross = calc.calculateGross();
    BigDecimal commission = calc.calculateCommission();
    BigDecimal tax = calc.calculateTax();
    BigDecimal total = calc.calculateTotal();

    grossLabel.setText(CurrencyUtil.formatNok(gross));
    commissionLabel.setText(CurrencyUtil.formatNok(commission));
    taxLabel.setText(CurrencyUtil.formatNok(tax));
    totalLabel.setText(CurrencyUtil.formatNok(total));

    BigDecimal cash = playerController.getPlayer().getCurrentMoney();
    if (total.compareTo(cash) > 0) {
      disableConfirm("Insufficient funds - you have "
          + CurrencyUtil.formatNok(cash));
    } else {
      enableConfirm();
    }
  }

  private BigDecimal parseQuantityOrZero() {
    String text = quantityField.getText();
    if (text == null || text.isBlank()) {
      return BigDecimal.ZERO;
    }
    try {
      return new BigDecimal(text.trim());
    } catch (NumberFormatException e) {
      return BigDecimal.ZERO;
    }
  }

  private void onConfirm() {
    try {
      BigDecimal quantity = parseQuantityOrZero();
      exchangeController.buy(stock.getSymbol(), quantity);
      stage.close();
      // Receipt modal will be shown by the caller, since showAndWait() returns the Transaction.
    } catch (RuntimeException ex) {
      fundsWarning.setText("Purchase failed: " + ex.getMessage());
      fundsWarning.setVisible(true);
      fundsWarning.setManaged(true);
    }
  }

  private void enableConfirm() {
    confirmButton.setDisable(false);
    confirmButton.setStyle(STYLE_BUTTON_OK);
    fundsWarning.setVisible(false);
    fundsWarning.setManaged(false);
  }

  private void disableConfirm(String message) {
    confirmButton.setDisable(true);
    confirmButton.setStyle(STYLE_BUTTON_OFF);
    fundsWarning.setText(message);
    fundsWarning.setVisible(true);
    fundsWarning.setManaged(true);
  }
}
