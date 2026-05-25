package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.model.Purchase;
import edu.ntnu.idi.idatt2003.model.Transaction;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import java.math.BigDecimal;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Reusable receipt modal shown after every trade and from transaction history.
 * Handles both Purchase and Sale transactions.
 */
public class TransactionReceiptModal {
  private static final String STYLE_BACKGROUND = "-fx-background-color: #0D1117;";
  private static final String STYLE_PANEL = "-fx-background-color: #161B22;"
      + " -fx-border-color: #30363D; -fx-border-width: 1;";
  private static final String STYLE_LABEL = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: monospace; -fx-font-size: 11px;";
  private static final String STYLE_VALUE = "-fx-text-fill: #E6EDF3;"
      + " -fx-font-family: monospace; -fx-font-size: 12px;";
  private static final String STYLE_TOTAL = "-fx-text-fill: #3FB950;"
      + " -fx-font-family: monospace;"
      + " -fx-font-size: 14px; -fx-font-weight: bold;";
  private static final String STYLE_BUTTON = "-fx-background-color: #1F6FEB;"
      + " -fx-text-fill: white; -fx-font-family: monospace;"
      + " -fx-font-weight: bold; -fx-padding: 10 24;";

  private TransactionReceiptModal() {}

  /**
   * Shows a receipt modal for the given transaction and waits for dismissal.
   *
   * @param transaction the completed transaction to display
   */
  public static void showAndWait(Transaction transaction) {
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setResizable(false);

    boolean isPurchase = transaction instanceof Purchase;
    String typeLabel = isPurchase ? "PURCHASE CONFIRMED" : "SALE CONFIRMED";
    String typeColor = isPurchase ? "#3FB950" : "#58A6FF";

    Label title = new Label(typeLabel);
    title.setStyle(
        "-fx-text-fill: " + typeColor + ";"
        + " -fx-font-family: monospace;"
        + " -fx-font-size: 15px; -fx-font-weight: bold;"
    );

    GridPane grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(8);
    grid.setPadding(new Insets(14));
    grid.setStyle(STYLE_PANEL);

    int row = 0;
    addRow(grid, row++, "Stock", transaction.getShare().stock().getSymbol());
    addRow(grid, row++, "Quantity", transaction.getShare().quantity().toPlainString() + " shares");
    addRow(grid, row++, "Week", String.valueOf(transaction.getWeek()));
    addRow(grid, row++, "Gross", CurrencyUtil.formatNok(transaction.getGrossValue()), STYLE_VALUE);
    addRow(grid, row++, "Commission",
        CurrencyUtil.formatNok(transaction.getCommission()), STYLE_VALUE);
    addRow(grid, row++, "Tax", CurrencyUtil.formatNok(transaction.getTax()), STYLE_VALUE);

    if (isPurchase) {
      addRow(grid, row++, "Total paid",
          CurrencyUtil.formatNok(transaction.getTotalValue()), STYLE_TOTAL);
    } else {
      BigDecimal costBasis = transaction.getShare().purchasePrice()
          .multiply(transaction.getShare().quantity());
      BigDecimal pnl = transaction.getTotalValue().subtract(costBasis);
      boolean profit = pnl.compareTo(BigDecimal.ZERO) >= 0;
      String pnlColor = profit ? "#3FB950" : "#F85149";
      String pnlText = (profit ? "+" : "-") + CurrencyUtil.formatNok(pnl.abs());

      addRow(grid, row++, "Net proceeds",
          CurrencyUtil.formatNok(transaction.getTotalValue()), STYLE_TOTAL);
      addRow(grid, row, "P&L vs purchase", pnlText,
          "-fx-text-fill: " + pnlColor + "; -fx-font-family: monospace;"
          + " -fx-font-size: 12px; -fx-font-weight: bold;");
    }

    Button doneButton = new Button("DONE");
    doneButton.setStyle(STYLE_BUTTON);
    doneButton.setMaxWidth(Double.MAX_VALUE);
    doneButton.setOnAction(event -> stage.close());

    VBox root = new VBox(14, title, grid, doneButton);
    root.setPadding(new Insets(20));
    root.setStyle(STYLE_BACKGROUND);

    stage.setScene(new Scene(root, 360, isPurchase ? 360 : 400));
    stage.setTitle(isPurchase ? "Purchase Receipt" : "Sale Receipt");
    stage.showAndWait();
  }

  /**
   * Adds labelled row to the breakdown grid using the default value style.
   *
   * @param grid the GridPane to add the row
   * @param row the row index
   * @param label the left column label text
   * @param value the right column value  text
   */

  private static void addRow(GridPane grid, int row, String label, String value) {
    addRow(grid, row, label, value, STYLE_VALUE);
  }

  /**
   * Adds labelled row to the breakdown grid with a custom value style.
   *
   * @param grid the GridPane to add the row
   * @param row the row index
   * @param label the left column label text
   * @param value the right column value  text
   * @param valueStyle the CSS style to apply to the value label
   */

  private static void addRow(GridPane grid, int row,
                             String label, String value, String valueStyle) {
    Label lbl = new Label(label);
    lbl.setStyle(STYLE_LABEL);

    Label lblValue = new Label(value);
    lblValue.setStyle(valueStyle);

    Region spacer = new Region();
    GridPane.setHgrow(spacer, Priority.ALWAYS);

    grid.add(lbl, 0, row);
    grid.add(spacer, 1, row);
    grid.add(lblValue, 2, row);
  }

}
