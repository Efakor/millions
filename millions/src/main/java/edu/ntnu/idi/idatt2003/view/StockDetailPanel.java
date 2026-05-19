package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import javafx.scene.chart.LineChart;
import javafx.geometry.Insets;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import edu.ntnu.idi.idatt2003.model.Stock;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.math.BigDecimal;

import java.util.List;
import java.util.stream.IntStream;


public class StockDetailPanel extends VBox {
  private Stock currentStock;
  private LineChart<Number, Number> lineChart;
  private final ExchangeController exchangeController;
  private final PlayerController playerController;
  private final Button buyButton = new Button();

  //Labels
  private final Label symbolLabel = new Label();
  private final Label priceLabel = new Label();
  private final Label companyLabel = new Label();
  private final Label highestPriceLabel = new Label();
  private final Label lowestPriceLabel = new Label();
  private final Label latestChangeLabel = new Label();


  public StockDetailPanel(ExchangeController exchangeController, PlayerController playerController) {
    this.exchangeController = exchangeController;
    this.playerController = playerController;
    buyButton.setOnAction(e -> {
      if (currentStock != null) {
        BuyDialog.showAndWait(currentStock,exchangeController,playerController);
      }
    });
    setSpacing(10);
    setPadding(new Insets(10));
    //panel background
    setStyle("-fx-background-color: #0D1117;");
    //labels
    symbolLabel.setStyle("-fx-text-fill: #E6EDF3;");
    companyLabel.setStyle("-fx-text-fill: #E6EDF3;");
    priceLabel.setStyle("-fx-text-fill: #E6EDF3;");
    highestPriceLabel.setStyle("-fx-text-fill: #E6EDF3;");
    lowestPriceLabel.setStyle("-fx-text-fill: #E6EDF3;");
    latestChangeLabel.setStyle("-fx-text-fill: #E6EDF3");
    //buy button
    buyButton.setStyle("-fx-background-color: #1F6FEB;"+"-fx-text-fill: white;"+"-fx-background-radius:6;");
    //Title
    Label titleLabel = new Label ("Stock details");
    titleLabel.setStyle("-fx-text-fill:#E6EDF3;+"+ "-fx-font-weight:bold;"+"-fx-font-size: 16;");
    getChildren().addAll(titleLabel,
        symbolLabel, priceLabel, highestPriceLabel, lowestPriceLabel, latestChangeLabel,buyButton);

  }
  public void showStock(Stock stock) {
    this.currentStock = stock;
    if (stock == null) {
      setVisible(false);
      setManaged(false);
      return;

    }
    buildChart(stock);

    symbolLabel.setText("Symbol: "+ stock.getSymbol());
    priceLabel.setText("Price: "+stock.getSalesPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
    companyLabel.setText("Company: "+ stock.getCompany());
    highestPriceLabel.setText("Highest Price: "+stock.getHighestPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
    lowestPriceLabel.setText("Lowest Price: "+stock.getLowestPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
    latestChangeLabel.setText("Latest price change: "+stock.getLatestPriceChange().setScale(2, BigDecimal.ROUND_HALF_UP)) ;
    setVisible(true);
    setManaged(true);
  }
  public Stock getcurrentStock() {
    return currentStock;
  }
  public Button getBuyButton() {
    return buyButton;

  }
  private void buildChart(Stock stock) {
    NumberAxis xAxis = new NumberAxis();
    xAxis.setLabel("Week");
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Price");

    LineChart<Number, Number> graph = new LineChart<Number, Number>(xAxis, yAxis);
    graph.setTitle("Historical Prices for " + stock.getSymbol());
    graph.setPrefHeight(200);

    //define a series
    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName(stock.getSymbol());


    List<BigDecimal> prices = stock.getHistoricalPrices();
    IntStream.range(0, prices.size()).forEach(i ->
        series.getData().add(new XYChart.Data<>(i + 1, prices.get(i).doubleValue())
        ));
    graph.getData().add(series);
    graph.setStyle("-fx-background-color:transparent");
    graph.setLegendVisible(false);
    graph.setCreateSymbols(false);

    xAxis.setTickLabelFill(javafx.scene.paint.Color.web("#8B949E"));
    yAxis.setTickLabelFill(javafx.scene.paint.Color.web("#8B949E"));
    xAxis.setStyle("-fx-tick-label-fill: #8B949E;");
    yAxis.setStyle("-fx-tick-label-fill: #8B949E;");

    if (lineChart != null) {
      getChildren().remove(lineChart);
    }
    lineChart = graph;
    getChildren().add(lineChart);


  }


}
