package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import edu.ntnu.idi.idatt2003.model.Stock;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;


public class StockDetailPanel extends VBox {
  private Stock currentStock;
  private final ExchangeController exchangeController;
  private final PlayerController playerController;
  private final Button buyButton = new Button("BUY");
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

}
