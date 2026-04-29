package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.model.Exchange;
import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.math.BigDecimal;


public class StockListView extends VBox implements GameObserver {
  private final Exchange exchange;
  private final TextField searchField=new TextField();
  private final TableView<Stock> stockTable=new TableView<>();
  private StockDetailPanel detailPanel;
  public StockListView(Exchange exchange) {
    this.exchange = exchange;
    createRoot();
    createTable();
    createSearchField();
    loadData();
    setupSelectionListener();
    getChildren().addAll(searchField,stockTable);
    exchange.addObserver(this);
  }

  private void createRoot(){
    setSpacing(10);
    setPrefWidth(380);
    setMinWidth(360);
    setMaxWidth(420);
    setPadding(new Insets(10));
    VBox.setVgrow(stockTable, Priority.ALWAYS);
    stockTable.setPrefHeight(420);
    stockTable.setMaxHeight(420);
    stockTable.setMinHeight(420);
    stockTable.setStyle("-fx-background-color: 161B22;"); //Used gemini in this, due to color problems
    stockTable.setRowFactory(color->{
      TableRow<Stock> row=new TableRow<>();
      row.setStyle("-fx-background-color: #161B22;-fx-text-background-color: white;");
      return row;
    }
    );
  }

  private void createSearchField(){
    searchField.setPromptText("Search by symbol or company");
  }
  private void createTable(){
    TableColumn<Stock,String> symbolColumn=new TableColumn<>("Symbol");
    TableColumn<Stock,String> companyColumn=new TableColumn<>("Company");
    TableColumn<Stock, BigDecimal> salesPriceColumn=new TableColumn<>("Price");
    TableColumn<Stock, BigDecimal> changeColumn=new TableColumn<>("Change");


    symbolColumn.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().getSymbol()));
    companyColumn.setCellValueFactory(cell->new SimpleStringProperty(cell.getValue().getCompany()));

    salesPriceColumn.setCellValueFactory(cell->new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getSalesPrice()));
    salesPriceColumn.setCellFactory(column->new TableCell<Stock, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal value, boolean empty) {
              super.updateItem(value, empty);
              if (empty || value == null) {
                setText(null);
                setStyle("");
                return;
              }
              setText(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
    });
    changeColumn.setCellValueFactory(cell->new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().
        getLatestPriceChange()));
    changeColumn.setCellFactory(column->new TableCell<Stock, BigDecimal>() {
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
        setText(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
      }
    });

    stockTable.getColumns().
        addAll(symbolColumn, companyColumn, salesPriceColumn, changeColumn);

    symbolColumn.setPrefWidth(50);
    companyColumn.setPrefWidth(120);
    salesPriceColumn.setPrefWidth(95);
    changeColumn.setPrefWidth(95);
  }


  private void loadData(){
    //Normal stock list-> JavaFX list
    ObservableList<Stock> stockList= FXCollections.observableArrayList(exchange.getAllStocks());
    FilteredList<Stock> filteredStockList=new FilteredList<>(stockList, stock -> true);
    //Search
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      String search=newValue==null?"":newValue.toLowerCase().trim();
      filteredStockList.setPredicate(stock -> {
        if(search==null || search.isEmpty()){
          return true;
        }
        return stock.getSymbol().toLowerCase().contains(search) ||stock.getCompany().toLowerCase().contains(search);
      });
    });
    stockTable.setItems(filteredStockList);
  }
  public void setupSelectionListener(){
    stockTable.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newStock)->{
      if(newStock !=null && detailPanel !=null){
      detailPanel.showStock(newStock);
      }
    });
  }
  public void setDetailPanel(StockDetailPanel detailPanel) {
    this.detailPanel=detailPanel;
  }

  @Override
  public void onGameEvent(GameEvent gameEvent) {
    if (gameEvent == GameEvent.WEEK_ADVANCED) {
      loadData();

      }
    }
      }
