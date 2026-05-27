package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.model.Purchase;
import edu.ntnu.idi.idatt2003.model.Transaction;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import java.math.BigDecimal;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Filterable transaction history panel.
 * Shows all trades with filter buttons for All, Sales, Purchases, and by wee.
 * Registers as a GameObserver and refreshes after every trade.
 */
public class TransactionHistoryView extends BorderPane implements GameObserver {
  private static final String STYLE_BACKGROUND = "-fx-background-color: #0D1117;";
  private static final String STYLE_HEADER = "-fx-background-color: #161B22;"
      + " -fx-border-color: #21262D; -fx-border-width: 0 0 1 0;";
  private static final String STYLE_ROW = "-fx-border-color: #21262D;"
      + " -fx-border-width: 0 0 1 0;";
  private static final String STYLE_LABEL = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: monospace; -fx-font-size: 10px;";
  private static final String STYLE_VALUE = "-fx-text-fill: #E6EDF3;"
      + " -fx-font-family: monospace; -fx-font-size: 11px;";
  private static final String STYLE_BUTTON_ON = "-fx-background-color: #1F6FEB;"
      + " -fx-text-fill: white; -fx-font-family: monospace;"
      + " -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 5;";
  private static final String STYLE_BUTTON_OFF = "-fx-background-color: #21262D;"
      + " -fx-text-fill: #8B949E; -fx-font-family: monospace;"
      + " -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 5;";

  private final PlayerController playerController;
  private final VBox rowContainer = new VBox();

  private String activeFilter = "ALL";
  private int weekFilter = 0; // 0 = all weeks

  private final Button buttonAll = new Button("ALL");
  private final Button buttonBuy = new Button("PURCHASES");
  private final Button buttonSell = new Button("SALES");
  private final Spinner<Integer> weekSpinner = new Spinner<>(0, 999, 0);

  /**
   * Constructs the transaction history view.
   *
   * @param playerController the player controller
   */
  public TransactionHistoryView(PlayerController playerController) {
    this.playerController = playerController;
    buildLayout();
  }
  /**
   * Builds the full panel layout including the filter bar,
   * scrollable transaction rows and initial data load.
   */

  private void buildLayout() {
    setStyle(STYLE_BACKGROUND);
    setTop(buildFilterBar());

    rowContainer.setSpacing(0);
    rowContainer.setFillWidth(true);

    ScrollPane scroll = new ScrollPane(rowContainer);
    scroll.setFitToWidth(true);
    scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scroll.setStyle("-fx-background-color: transparent; -fx-background: #0D1117;");
    setCenter(scroll);

    refresh();
  }
  /**
   * Builds the filter bar containing type filter buttons,
   * a week spinner and a hint label.
   *
   * @return an HBox containing all filter controls
   */

  private HBox buildFilterBar() {
    buttonAll.setStyle(STYLE_BUTTON_ON);
    buttonBuy.setStyle(STYLE_BUTTON_OFF);
    buttonSell.setStyle(STYLE_BUTTON_OFF);

    buttonAll.setOnAction(event -> setFilter("ALL"));
    buttonBuy.setOnAction(event -> setFilter("BUY"));
    buttonSell.setOnAction(event -> setFilter("SELL"));

    weekSpinner.setEditable(true);
    weekSpinner.setPrefWidth(80);
    weekSpinner.setStyle("-fx-font-family: monospace; -fx-font-size: 10px");
    weekSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
      weekFilter = newValue;
      refresh();
    });

    Label weekLabel = new Label("WEEK:");
    weekLabel.setStyle(STYLE_LABEL);

    Label hint = new Label("(0 = All)");
    hint.setStyle(STYLE_LABEL);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox bar = new HBox(8, buttonAll, buttonBuy, buttonSell, spacer, weekLabel, weekSpinner, hint);
    bar.setPadding(new Insets(8, 12, 8, 12));
    bar.setAlignment(Pos.CENTER_LEFT);
    bar.setStyle(STYLE_HEADER);
    return bar;
  }
  /**
   * Sets the active transaction type filter and refreshes the displayed rows.
   * Updates button styles to reflect the active filter.
   *
   * @param filter the filter to apply : "ALL", "BUY" or "SELL"
   */

  private void setFilter(String filter) {
    activeFilter = filter;
    buttonAll.setStyle(filter.equals("ALL") ? STYLE_BUTTON_ON : STYLE_BUTTON_OFF);
    buttonBuy.setStyle(filter.equals("BUY") ? STYLE_BUTTON_ON : STYLE_BUTTON_OFF);
    buttonSell.setStyle(filter.equals("SELL") ? STYLE_BUTTON_ON : STYLE_BUTTON_OFF);
    refresh();
  }

  /**
   * Called by Exchange on every game event - refreshes the table.
   *
   * @param gameEvent the event that was triggered
   */
  @Override
  public void onGameEvent(GameEvent gameEvent) {
    refresh();
  }
  /**
   * Refreshes the transaction list by applying the active type filter
   * and week filter, then rebuilding all displayed rows.
   * Shows an empty state message if no transactions match the filters.
   */

  private void refresh() {
    rowContainer.getChildren().clear();

    List<Transaction> all = playerController.getPlayer()
        .getTransactionArchive()
        .getAllTransactions();

    // filtering by type
    List<Transaction> filtered = all.stream()
        .filter(t -> switch (activeFilter) {
          case "BUY" -> t instanceof Purchase;
          case "SELL" -> !(t instanceof Purchase);
          default -> true;
        })
        .toList();

    // filtering by week
    if (weekFilter > 0) {
      filtered = filtered.stream()
          .filter(t -> t.getWeek() == weekFilter)
          .toList();
    }

    if (filtered.isEmpty()) {
      Label empty = new Label("No transactions found");
      empty.setStyle(STYLE_LABEL);
      empty.setPadding(new Insets(20, 12, 20, 12));
      rowContainer.getChildren().add(empty);
      return;
    }

    // column headers
    rowContainer.getChildren().add(buildHeaderRow());

    // data rows
    for (int i = 0; i < filtered.size(); i++) {
      rowContainer.getChildren().add(buildRow(i + 1, filtered.get(i)));
    }
  }

  /**
   * Builds the column header row for the transaction table.
   *
   * @return a GridPane containing the column header labels
   */
  private GridPane buildHeaderRow() {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setPadding(new Insets(6, 12, 6, 12));
    grid.setStyle("-fx-background-color: #161B22;"
        + " -fx-border-color: #21262D; -fx-border-width: 0 0 1 0;");

    String[] headers = {"#", "WEEKS", "TYPE", "SYMBOL", "QTY", "GROSS", "FEES", "TOTAL"};
    for (int i = 0; i < headers.length; i++) {
      Label label = new Label(headers[i]);
      label.setStyle(STYLE_LABEL);
      grid.add(label, i, 0);
    }
    return grid;
  }

  /**
   * Builds a single transaction data row shows index,week,type,
   * symbol,quantity,gross value,fees,total value.
   *
   * @param index the row number displayed in the first column
   * @param t the transaction to display
   * @return a GridPane containing the formatted transaction data
   */
  private GridPane buildRow(int index, Transaction t) {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setPadding(new Insets(8, 12, 8, 12));
    grid.setStyle(STYLE_ROW);
    grid.setOnMouseClicked(event -> TransactionReceiptModal.showAndWait(t));

    boolean isPurchase = t instanceof Purchase;
    String type = isPurchase ? "BUY" : "SELL";
    String typeColor = isPurchase ? "#3FB950" : "#F85149";
    BigDecimal fees = t.getCommission().add(t.getTax());

    String[] values = {
        String.valueOf(index),
        String.valueOf(t.getWeek()),
        type,
        t.getShare().stock().getSymbol(),
        t.getShare().quantity().toPlainString(),
        CurrencyUtil.formatNok(t.getGrossValue()),
        CurrencyUtil.formatNok(fees),
        CurrencyUtil.formatNok(t.getTotalValue())
    };

    for (int i = 0; i < values.length; i++) {
      Label label = new Label(values[i]);
      if (i == 2) {
        label.setStyle("-fx-text-fill: " + typeColor + ";"
            + " -fx-font-family: monospace; -fx-font-size: 11px; -fx-font-weight: bold;");
      } else {
        label.setStyle(STYLE_VALUE);
      }
      grid.add(label, i, 0);
    }
    return grid;
  }
}

