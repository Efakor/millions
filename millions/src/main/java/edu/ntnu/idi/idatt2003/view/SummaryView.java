package edu.ntnu.idi.idatt2003.view;


import edu.ntnu.idi.idatt2003.model.Player;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class SummaryView extends VBox {
  private final Player player;



  public SummaryView(Player player,int weeksPlayed) {
    this.player=player;
    setSpacing(15);
    setPadding(new Insets(30));
    setStyle("-fx-background-color:black;");

    buildUI(player,weeksPlayed);
  }
  private void buildUI(Player player,int weeksPlayed) {
    Label nameLabel=new Label("Name:"+player.getName());
    nameLabel.setStyle("-fx-font-size:20; -fx-text-fill: white;");
    Label netWorthLabel=new Label("Net Worth:"+player.getNetWorth());
    netWorthLabel.setStyle("-fx-font-size:20; -fx-text-fill: white;");
    Label profitLossLabel=new Label("Profit/Loss:"+player.getCurrentMoney().subtract(player.getStartingMoney()));
    profitLossLabel.setStyle("-fx-font-size:20 ; -fx-text-fill: white;");
    Label statusLabel=new Label("Status:"+player.getStatus());
    statusLabel.setStyle("-fx-font-size:20 ; -fx-text-fill: white;");
    Label weekLabel=new Label("Week:"+weeksPlayed);
    weekLabel.setStyle("-fx-font-size:20; -fx-text-fill: white;");
    Button exitButton=new Button("EXIT");
    exitButton.setOnAction(event -> {
      Platform.exit();});

    getChildren().addAll(nameLabel,netWorthLabel,profitLossLabel,statusLabel,weekLabel);
    getChildren().add(exitButton);
  }

}
