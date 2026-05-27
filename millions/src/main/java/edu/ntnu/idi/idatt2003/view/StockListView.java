package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Panel displaying a searchable, scrollable list of all stocks on the exchange.
 * Implements {@link GameObserver} to automatically refresh after each week advance.
 * Search delegates to {@link ExchangeController#findStocks(String)} for clean MVC separation.
 */
public class StockListView extends VBox implements GameObserver {
  private final ExchangeController exchangeController;
  private final TextField searchField = new TextField();
  private final TableView<Stock> stockTable = new TableView<>();
  private StockDetailPanel detailPanel;

  /**
   * Constructs the initial stock list view and registers it as a GameObserver.
   *
   * @param exchangecontroller the exchange controller providing the stock data
   */
  public StockListView(ExchangeController exchangecontroller) {
    this.exchangeController = exchangecontroller;
    createRoot();
    createTable();
    createSearchField();
    loadData();
    setupSelectionListener();
    getChildren().addAll(searchField, stockTable);
    exchangeController.getExchange().addObserver(this);
  }

  /**
   * Configures the root VBox layout properties including size constraints,
   * padding and table row styling.
   */

  private void createRoot() {
    setSpacing(10);
    setPrefWidth(380);
    setMinWidth(360);
    setMaxWidth(420);
    setPadding(new Insets(10));
    VBox.setVgrow(stockTable, Priority.ALWAYS);
    stockTable.setPrefHeight(420);
    stockTable.setMaxHeight(420);
    stockTable.setMinHeight(420);
    // Used gemini in this, due to color problems
    stockTable.setStyle("-fx-background-color:#161B22;");
    stockTable.setRowFactory(color -> {
      TableRow<Stock> row = new TableRow<>();
      row.setStyle("-fx-background-color: #161B22;-fx-text-background-color: white;");
      return row;
    });
  }

  /**
   * Configures the search field with placeholder text.
   */
  private void createSearchField() {
    searchField.setPromptText("Search by symbol or company");
  }

  /**
   * Creates and configures the stock table columns for symbol, company,
   * price and weekly change. Applies colour coding to the change column
   * green for positive, red for negative.
   */
  private void createTable() {
    TableColumn<Stock, String> symbolColumn = new TableColumn<>("Symbol");
    symbolColumn.setCellValueFactory(cell ->
        new SimpleStringProperty(cell.getValue().getSymbol()));

    TableColumn<Stock, String> companyColumn = new TableColumn<>("Company");
    companyColumn.setCellValueFactory(cell ->
        new SimpleStringProperty(cell.getValue().getCompany()));

    TableColumn<Stock, BigDecimal> salesPriceColumn = new TableColumn<>("Price");
    TableColumn<Stock, BigDecimal> changeColumn = new TableColumn<>("Change");

    salesPriceColumn.setCellValueFactory(cell ->
        new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getSalesPrice()));
    salesPriceColumn.setCellFactory(column ->
        new TableCell<>() {
          @Override
          protected void updateItem(BigDecimal value, boolean empty) {
            super.updateItem(value, empty);
            if (empty || value == null) {
              setText(null);
              setStyle("");
              return;
            }
            setText(value.setScale(2, RoundingMode.HALF_UP).toString());
          }
        });
    changeColumn.setCellValueFactory(cell ->
        new javafx.beans.property.SimpleObjectProperty<>(cell.getValue()
                .getLatestPriceChange()));
    changeColumn.setCellFactory(column ->
        new TableCell<>() {
          @Override
          protected void updateItem(BigDecimal value, boolean empty) {
            super.updateItem(value, empty);
            if (empty || value == null) {
              setText(null);
              setStyle("");
              return;
            }
            setText(value.toString());
            if (value.compareTo(BigDecimal.ZERO) > 0) {
              setStyle("-fx-text-fill: green;");
            } else if (value.compareTo(BigDecimal.ZERO) < 0) {
              setStyle("-fx-text-fill: red;");

            } else {
              setStyle("-fx-text-fill: white;");
            }
            setText(value.setScale(2, RoundingMode.HALF_UP).toString());
          }
        });

    stockTable.getColumns().addAll(symbolColumn, companyColumn, salesPriceColumn, changeColumn);

    symbolColumn.setPrefWidth(50);
    companyColumn.setPrefWidth(120);
    salesPriceColumn.setPrefWidth(95);
    changeColumn.setPrefWidth(95);
  }

  /**
   * Loads all stocks into the table and attaches a search listener.
   * When the search field is empty, all stocks are shown.
   * When the user types, results are filtered via
   * {@link ExchangeController#findStocks(String)}.
   */

  private void loadData() {
    // Normal stock list-> JavaFX list
    stockTable.setItems(FXCollections.observableArrayList(exchangeController.getAllStocks()));
    // Search
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {

      if (newValue == null || newValue.trim().isEmpty()) {
        stockTable.setItems(FXCollections.observableArrayList(
            exchangeController.getAllStocks()
        ));

      } else {
        stockTable.setItems(FXCollections.observableArrayList(
            exchangeController.findStocks(newValue.trim())));
      }

    });

  }

  /**
   * Attaches a selection listener to the table so that  clicking a stock
   * triggers on the detail panel.
   */
  public void setupSelectionListener() {
    stockTable.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newStock) -> {
          if (newStock != null && detailPanel != null) {
            detailPanel.showStock(newStock);
          }
        });
  }

  /**
   * Sets the stock detail panel to notify when a stock is selected.
   *
   * @param detailPanel the detail panel to update on selection
   */
  public void setDetailPanel(StockDetailPanel detailPanel) {
    this.detailPanel = detailPanel;
  }

  /**
   * Called by the exchange when a game event is fired.
   * Refreshes the table display on {@link GameEvent#WEEK_ADVANCED}
   * to reflect updated stock prices.
   *
   * @param gameEvent the game event that was triggered
   */

  @Override
  public void onGameEvent(GameEvent gameEvent) {
    if (gameEvent == GameEvent.WEEK_ADVANCED) {
      stockTable.refresh();

    }
  }
}
