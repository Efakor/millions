package edu.ntnu.idi.idatt2003.view;


import edu.ntnu.idi.idatt2003.controller.ExchangeController;

import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class GainersLosersPanel extends VBox implements GameObserver {
  private ExchangeController exchangeController;
  private VBox gainersList;
  private VBox losersList;
  private Spinner <Integer> limitSpinner;

  public GainersLosersPanel(ExchangeController exchangeController) {
    setSpacing(10);
    setMaxWidth(500);
    setPrefWidth(500);
    setPadding(new Insets(10));

    this.limitSpinner = new Spinner<>(1,20,5);
    gainersList=new VBox(5);
    losersList=new VBox(5);
    gainersList.setPrefWidth(300);
    losersList.setPrefWidth(300);
    this.exchangeController=exchangeController;
    //Titles
    Label gainersTitle= new Label("TOP GAINERS");
    gainersTitle.setStyle("-fx-text-fill:green; -fx-font-weight:bold");
    gainersList.getChildren().add(gainersTitle);
    Label loserTitle=new Label("LOSERS");
    loserTitle.setStyle("-fx-text-fill:red; -fx-font-weight:bold");
    losersList.getChildren().add(loserTitle);
    //Add to the HBOX
    HBox lists=new HBox(10,gainersList,losersList);
    getChildren().addAll(limitSpinner,lists);
    limitSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {refresh();});
    exchangeController.getExchange().addObserver(this);
    refresh();
  }
  private void refresh(){
    //To show minimum stock
    int limit=limitSpinner.getValue();
    List<Stock>losers=exchangeController.getLosers(limit);
    List<Stock>gainers=exchangeController.getGainers(limit);
    //The losers and gainers refreshes,but the title stays
    losersList.getChildren().subList(1,losersList.getChildren().size()).clear();
    gainersList.getChildren().subList(1,gainersList.getChildren().size()).clear();

    int rankCounter=1;
    for(Stock stock:gainers){
      HBox gainerBox=new HBox();
      gainerBox.setSpacing(10);
      Label rank=new Label("#"+rankCounter );
      rankCounter++;

      rank.setStyle("-fx-font-size: 11px ; -fx-font-size:bold;-fx-text-fill:white");
      Label symbol=new Label(stock.getSymbol());
      symbol.setStyle("-fx-font-size: 11px ; -fx-text-fill:white");
      String companyName = stock.getCompany();
      if (companyName.length() > 10) {
        companyName = companyName.substring(0, 10) + "...";
      }
      Label company=new Label(companyName );
      company.setStyle("-fx-font-size: 11px ; -fx-text-fill:white");
      //Absolute Price Change
      Label absolutePriceChange=new Label("+"+stock.getLatestPriceChange().setScale(2, RoundingMode.HALF_UP) );
      absolutePriceChange.setStyle("-fx-font-size: 11px ; -fx-text-fill:green");
      //Percentage
      BigDecimal now=stock.getSalesPrice();
      BigDecimal change=stock.getLatestPriceChange();
      //percentage=change/(change-now)*100
      BigDecimal difference=now.subtract(change);
      BigDecimal percentage=change.divide(difference,4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

      String percentageText=String.format("%.1f%%",percentage.doubleValue());
      Label percentChangeLabel=new Label(percentageText);
      percentChangeLabel.setStyle("-fx-font-size: 11px ; -fx-text-fill:green");
      gainerBox.getChildren().addAll(rank,symbol,company,absolutePriceChange,percentChangeLabel);
      gainersList.getChildren().add(gainerBox);



      }
    int lossCounter=1;
    for(Stock stock:losers){
      HBox loserBox=new HBox();
      loserBox.setSpacing(10);
      Label rank=new Label("#"+lossCounter );
      lossCounter++;
      rank.setStyle("-fx-font-size: 11px; -fx-font-weight:bold; -fx-text-fill:white" );
      Label symbol=new Label(stock.getSymbol());
      symbol.setStyle("-fx-font-size: 11px; -fx-text-fill:white");
      String companyName = stock.getCompany();
      if (companyName.length() > 10) {
        companyName = companyName.substring(0, 10) + "...";
      }
      Label company=new Label(companyName );
      company.setStyle("-fx-font-size: 11px; -fx-text-fill:white");
      Label absolutePriceChange=new Label(stock.getLatestPriceChange().setScale(2, RoundingMode.HALF_UP).toString());
      absolutePriceChange.setStyle("-fx-font-size:11px ;-fx-text-fill:red");

      //Percentage calculations
      BigDecimal now=stock.getSalesPrice();
      BigDecimal change=stock.getLatestPriceChange();
      BigDecimal difference=now.subtract(change);

      BigDecimal percentage=change.divide(difference,4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

      Label percentChangeLabel=new Label (String.format("%.1f%%",percentage.doubleValue()));
      percentChangeLabel.setStyle("-fx-font-size: 11px ; -fx-text-fill:green");
      loserBox.getChildren().addAll(rank,symbol,company,absolutePriceChange,percentChangeLabel);
      losersList.getChildren().add(loserBox);




    }


  }
  @Override
  public void onGameEvent(GameEvent event){
    if(event==GameEvent.WEEK_ADVANCED){
      refresh();

    }
  }
  /**
   * Acceptance criteria:
   * • Two ranked lists: Top Gainers / Top Losers
   * • Each entry shows: rank, symbol, company, price change, % change
   * • Calls Exchange.getGainers(limit) and Exchange.getLosers(limit)
   * • Limit configurable (default 5, spinner to change)
   * • Refreshes after WEEK_ADVANCED event
   * • Colour-coded (green gainers, red losers)
   */
}
