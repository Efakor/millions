package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class SellAllDialog {
  private ExchangeController exchangeController;
  private PlayerController playerController;
  private final Stage stage;
  private Stage dialogStage;

  private SellAllDialog(ExchangeController exchangeController, PlayerController playerController, Stage stage) {
    this.exchangeController = exchangeController;
    this.playerController = playerController;
    this.stage = stage;
  }

  public static void show(ExchangeController exchangeController, PlayerController playerController, Stage stage) {
    new SellAllDialog(exchangeController, playerController, stage).buildUi();

  }

  private void buildUi() {
    dialogStage = new Stage();
    Label title = buildTitle();
    Label subtitle = buildSubtitle();
    VBox proceedBox = buildProceedBox();
    Label warning = buildWarning();
    HBox button=buildButton();

    VBox roots = new VBox(10);
    roots.setStyle("-fx-background-color:#161B22;");
    roots.getChildren().addAll(title, subtitle, proceedBox, warning, button);
    roots.setPadding(new Insets(20));
    Scene scene = new Scene(roots, 400, 400);




    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.setTitle("SELL ALL & EXIT");

    dialogStage.setScene(scene);
    dialogStage.showAndWait();
  }

  public Label buildTitle() {
    Label title = new Label("SELL ALL & EXIT");
    title.setStyle(" -fx-text-fill:red;-fx-font-size:20px;-fx-font-weight:bold;");
    return title;
  }

  private Label buildSubtitle() {
    Label subtitle = new Label("You are about  to sell all your holdings and and the game \n " +
        "Are you sure you want to sell everything?");
    subtitle.setStyle(" -fx-text-fill:#8B949E;-fx-font-size:13px");
    subtitle.setWrapText(true);
    return subtitle;
  }

  private Label buildWarning() {
    Label warningLabel = new Label("THIS CANNOT BE UNDONE");
    warningLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: red");
    return warningLabel;


  }

  private VBox buildProceedBox() {
    VBox proceedsBox = new VBox(10);
    proceedsBox.setStyle(
        "-fx-background-color:#0D1117;" +
            "-fx-border-color:#30363D;" +
            "-fx-border-width:1px;" +
            "-fx-border-radius:6;" +
            "-fx-background-radius:6"

    );
    proceedsBox.setPadding(new Insets(14));
    BigDecimal gross = playerController.getTotalGross();
    Label grossLabel = new Label("GROSS:" + CurrencyUtil.formatNok(gross));
    grossLabel.setStyle("-fx-font-size:11px; -fx-text-fill:white;");

    BigDecimal proceeds = playerController.getTotalProceeds();
    Label proceedLabel = new Label(" ESTIMATED PROCEEDS: " + CurrencyUtil.formatNok(proceeds));
    proceedLabel.setStyle("-fx-font-size:11px; -fx-text-fill:green;");


    BigDecimal commision = playerController.getTotalCommission();
    Label commisionLabel = new Label("COMMISION: " + CurrencyUtil.formatNok(commision));
    commisionLabel.setStyle("-fx-font-size:11px; -fx-text-fill:white;");



    BigDecimal tax = playerController.getTotalTax();
    Label taxLabel = new Label("Tax: " + CurrencyUtil.formatNok(tax));
    taxLabel.setStyle("-fx-font-size:11px; -fx-text-fill:white;");


    proceedsBox.getChildren().addAll(grossLabel, commisionLabel, taxLabel, new Separator(), proceedLabel);
    return proceedsBox;

  }

  private HBox buildButton() {
    Button confirmButton = new Button("YES AND EXIT");
    Button cancelButton = new Button("CANCEL");
    confirmButton.setOnAction(event -> {
      playerController.sellAll();
      dialogStage.close();
      SummaryView summaryView = new SummaryView(playerController.getPlayer(),
          exchangeController.getExchange().getWeek()
      );
      stage.getScene().setRoot(summaryView);
    });
    cancelButton.setOnAction(event -> {
      dialogStage.close();
    });


    cancelButton.setText("CANCEL");
    cancelButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white;-fx-background-color: transparent;-fx-padding:10 20;-fx-border-color:#30363D;-fx-border-width:1px;");
    confirmButton.setText("YES AND EXIT");
    confirmButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white;-fx-background-color:#8B0000;-fx-padding:10 20;");
    HBox buttons = new HBox(10, cancelButton, confirmButton);
    buttons.setAlignment(Pos.CENTER_RIGHT);
    return buttons;
  }
}


















