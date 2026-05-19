package edu.ntnu.idi.idatt2003.view;


import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;




public class  SummaryView extends VBox {
  private final PlayerController playerController;
  private final Stage stage;
  private Stage dialogStage;


  public SummaryView(PlayerController playerController, int weeksPlayed, Stage stage) {
    this.playerController = playerController;
    this.stage = stage;
    setSpacing(15);
    setPadding(new Insets(30));
    setStyle("-fx-background-color:#0D1117;");

    buildUI(weeksPlayed);
  }

  private void buildUI(int weeksPlayed) {
    Label subtitle = new Label("Game Over");
    subtitle.setStyle("-fx-font-size:13; -fx-text-fill: #8B949E;-fx-font-weight: bold;");
    Label title = new Label(" Final Summary");
    title.setStyle("-fx-font-size:20; -fx-text-fill: white;");
    Label description = new Label(String.format(" Player:%s .%d weeks played", playerController.getPlayerName(), weeksPlayed));
    description.setStyle("-fx-font-size:14; -fx-text-fill: white;");

    GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setAlignment(Pos.CENTER);
    gridPane.add(createCard("NET WORTH", CurrencyUtil.formatNok(playerController.getNetWorth())), 0, 0);
    gridPane.add(createCard("STARTING CAPITAL", CurrencyUtil.formatNok(playerController.getStartingCapital())), 1, 0);
    gridPane.add(createCard("PROFIT/LOSS", CurrencyUtil.formatNok(playerController.getProfitLoss())), 0, 1);
    gridPane.add(createCard("WEEKS PLAYED", String.valueOf(weeksPlayed)), 1, 1);
    HBox statusBox = buildStatusBox(playerController.getPlayerStatus());
    Button exitButton = buildExitButton();
    this.getChildren().addAll(
        subtitle, title, description, gridPane, statusBox, exitButton
    );


  }

  private VBox createCard(String title, String value) {
    VBox card = new VBox(5);
    card.setPadding(new Insets(15));
    card.setPrefWidth(200);
    card.setStyle("-fx-background-color: #161B22;-fx-border-color: #30363D;-fx-border-width: 6px;");
    Label titleLabel = new Label(title);
    titleLabel.setStyle("-fx-text-fill: white;-fx-font-size: 10px;");
    Label valueLabel = new Label(value);
    valueLabel.setStyle("-fx-text-fill: white;-fx-font-size: 20px;;-fx-font-weight: bold;");
    card.getChildren().addAll(titleLabel, valueLabel);
    return card;

  }

  private HBox buildStatusBox(String playerStatus) {
    HBox statusBox = new HBox(10);
    statusBox.setAlignment(Pos.CENTER_LEFT);
    statusBox.setPadding(new Insets(15));
    statusBox.setStyle("-fx-background-color: #161B22;");
    return statusBox;
  }


  private Button buildExitButton() {
    Button exitButton = new Button("EXIT");
    exitButton.setMaxWidth(Double.MAX_VALUE);
    exitButton.setPadding(new Insets(15));
    exitButton.setStyle("-fx-font-size: 10px;-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color:#8B0000");
    exitButton.setOnAction(event -> {
      Platform.exit();
    });
    return exitButton;
  }
}

