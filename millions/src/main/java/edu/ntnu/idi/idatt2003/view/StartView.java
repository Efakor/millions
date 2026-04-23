package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Objects;


/**
 * Placeholder start screen.
 */
public class StartView extends VBox {
  private final PlayerController playerController;
  private final Stage stage;

  //TextFields
  private final TextField playerNameField = new TextField();
  private final TextField startingCapitalField = new TextField();

  //Labels
  private final Label selectedFileLabel = new Label("No file selected");
  private final  Label errorLabel=new Label();
  private final Label nameErrorLabel=new Label();
  private final Label capitalErrorLabel=new Label();
  //Buttons
  private final Button chooseFileButton = new Button("CHOOSE FILE");
  private final Button startGameButton = new Button("START TRADING");



  private boolean nameTouched=false;
  private boolean capitalTouched=false;

  public StartView(PlayerController  playerController, Stage stage) {
    this.playerController = Objects.requireNonNull(playerController, "Player Controller cannot be null");
    this.stage= Objects.requireNonNull(stage, "Game started cannot be null");

    configureRoot();
    configureFields();
    configureButtons();
    buildLayout();
    bindEvents();
    updateFieldStyle();
    updateStartButtonState();

      }
      //Layout
      private void configureRoot() {
        setAlignment(Pos.CENTER);
        setFillWidth(false);
        setSpacing(20);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: #0D1117;");

      }
      private void buildLayout() {
        //Millions-headbar
        Label title = new Label("MILLIONS");
        title.setStyle("-fx-text-fill: #58A6FF;-fx-font-size: 16; -fx-font-weight: bold;");
        HBox titleBox = new HBox(title);
        titleBox.setStyle("-fx-background-color: #161B22;"+
            "-fx-border-color:#30363D;"+
            "-fx-border-radius:6 6 0 0;"+
            "-fx-background-radius:6 6 0 0;"+
            "-fx-padding: 12 24;");

        //Error label
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        errorLabel.setStyle("-fx-text-fill: #F85149;" +
            "-fx-background-color: #3B1219;"+
            "-fx-border-color:#F85149;"+
            "-fx-border-radius:4;"+
            "-fx-background-radius:4;"+
            "-fx-padding:8;"+
            "-fx-font-size:12px;");
        //Body(new game +form)
        VBox body = new VBox(16,createTitleBox(),createFormGrid(),errorLabel,startGameButton);
        body.setStyle("-fx-background-color: #161B22;"+
            "-fx-border-color:#30363D;"+
            "-fx-border-radius: 0 0 8 8;"+
            "-fx-padding:24;");

        //Wrap everything with a darker border
        VBox card=new VBox(0,titleBox,body);
        card.setPrefWidth(420);
        getChildren().add(card);
      }

      private VBox createTitleBox() {
        Label sectionTitle = new Label("NEW GAME");
        sectionTitle.setStyle("-fx-text-fill: #58A6FF;"+"-fx-font-size: 22px;"+ "-fx-font-weight: bold;");



        Label subtitle = new Label("Configure your trading session");
        subtitle.setStyle("-fx-text-fill:#8b949E;"+
            "-fx-font-size: 13px;");
        VBox box = new VBox(6,sectionTitle, subtitle);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
      }

      private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setMinWidth(160);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPrefWidth(280);
        grid.getColumnConstraints().addAll(column1, column2);

        Label nameLabel = createFormLabel("Player Name");
        Label capitalLabel = createFormLabel("Starting capital($)");

        grid.add(nameLabel, 0, 0);
        grid.add(playerNameField, 1, 0);
        grid.add(nameErrorLabel, 1, 1);
        grid.add(capitalLabel, 0, 2);
        grid.add(startingCapitalField, 1, 2);
        grid.add(capitalErrorLabel, 1, 3);


        return grid;


      }

      private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #E6EDF3;-fx-font-size: 13px;");
        return label;
      }


      //Fields and buttons configuration

      private void configureFields() {
        configureTextField(playerNameField, "Enter your name...");
        configureTextField(startingCapitalField, "10000");
        selectedFileLabel.setStyle("-fx-text-fill: #6E7681;" + "-fx-font-size: 11px;");

        nameErrorLabel.setStyle("-fx-text-fill:#F85149; -fx-font-size: 11px;");
        nameErrorLabel.setVisible(false);
        nameErrorLabel.setManaged(false);
        capitalErrorLabel.setStyle("-fx-text-fill:#F85149; -fx-font-size: 11px;");
        capitalErrorLabel.setVisible(false);
        capitalErrorLabel.setManaged(false);




      }

      private void configureTextField(TextField textField, String text) {
        textField.setPromptText(text);
        textField.setPrefWidth(280);
        applyNeutralFieldStyle(textField);
      }

      //Button configuration
      private void configureButtons() {
        startGameButton.setMaxWidth(Double.MAX_VALUE);
        startGameButton.setStyle("-fx-background-color: #1F6FEB;"+ "-fx-text-fill: white;"+
            "-fx-font-size: 13px;"+
            "-fx-padding:12;"
            +"-fx-border-radius:6;"+
            "-fx-background-radius:6;" +
            "-fx-cursor:hand;");
        chooseFileButton.setStyle("-fx-background-color:#21262D;"+
            "-fx-text-fill: #E6EDF3;"+
            "-fx-border-color:#30363D;"+
            "-fx-background-radius:6;"+
            "-fx-padding:6 12;"+
            "-fx-font-size:11px;"+
            "-fx-font-weight: bold;"+
            "-fx-cursor:hand;"
        );

        startGameButton.setOnAction(e -> handleStartGame());



      }
      //Field styles
      private void applyNeutralFieldStyle(TextField field) {
        field.setStyle(
            "-fx-background-color: #0D1117;" +
                "-fx-text-fill: #E6EDF3;" +
                "-fx-prompt-text-fill: #6E7681;" +
                "-fx-padding: 10;" +
                "-fx-border-color: #30363D;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );
      }
      private void applyErrorStyle(TextField field) {
        field.setStyle("-fx-background-color:#0D1117;" +
            "-fx-text-fill:#E6EDF3;" +
            "-fx-border-color:#F85149;"+
            "-fx-border-width:1;"+
            "-fx-background-radius: 6;" +
            "-fx-border-radius:6;"+
            "-fx-padding:10;");
      }

      private void applyValidStyle(TextField field) {
        field.setStyle("-fx-background-color:#0D1117;" +
            "-fx-text-fill:#E6EDF3;" +
            "-fx-border-color:#3FB950;"+
            "-fx-border-radius:6;"+
            "-fx-background-radius:6;"+
            "-fx-padding:10;");
      }
      //Events
      private void bindEvents() {
        playerNameField.textProperty().addListener((obs, oldValue, newValue) -> {
          nameTouched = true;
          hideError();
          updateFieldStyle();
          updateStartButtonState();
        });

        startingCapitalField.textProperty().addListener((obs, oldValue, newValue) -> {
          capitalTouched = true;
          hideError();
          updateFieldStyle();
          updateStartButtonState();
        });

      }
      private boolean isNameValid () {
        String name = playerNameField.getText().trim();
        return !name.isEmpty();
      }

      private boolean isCapitalValid() {
        String text = startingCapitalField.getText().trim();
        if (text.isEmpty()) {
          return false;
        }
        try {
          BigDecimal value = new BigDecimal(text);
          if(value.compareTo(BigDecimal.ZERO)<=0)throw new NumberFormatException();
          return true;

        } catch (NumberFormatException e) {
          return false;

        }
      }
      //Handlers

      private void handleStartGame() {
        nameTouched=true;
        capitalTouched=true;
        updateFieldStyle();

        boolean nameValid = isNameValid();
        boolean capitalValid = isCapitalValid();


        if (!nameValid) {
          return;
        }
        if(!capitalValid) {
          return;


        }
        String name = playerNameField.getText().trim();
        BigDecimal capital = new BigDecimal(startingCapitalField.getText().trim());
        playerController.newGame(name, capital);
        MainView mainView=new MainView(
            new ExchangeController(playerController.getExchange()),
            playerController);
        StockListView stockListView=new StockListView(playerController.getExchange());
        mainView.setStockListView(stockListView);
        stage.getScene().setRoot(mainView);
      }
      private void updateFieldStyle() {
        if (!nameTouched) {
          applyNeutralFieldStyle(playerNameField);
          nameErrorLabel.setVisible(false);
          nameErrorLabel.setManaged(false);
        } else if (isNameValid()) {
          applyValidStyle(playerNameField);
          nameErrorLabel.setVisible(false);
          nameErrorLabel.setManaged(false);
        } else {
          applyErrorStyle(playerNameField);
          nameErrorLabel.setText("Name cannot be empty");
          nameErrorLabel.setVisible(true);
          nameErrorLabel.setManaged(true);
        }
        if (!capitalTouched) {
          applyNeutralFieldStyle(startingCapitalField);
          capitalErrorLabel.setVisible(false);
          capitalErrorLabel.setManaged(false);
        } else if (isCapitalValid()) {
          applyValidStyle(startingCapitalField);
          capitalErrorLabel.setVisible(false);
          capitalErrorLabel.setManaged(false);
        } else {
          applyErrorStyle(startingCapitalField);
          capitalErrorLabel.setText("Enter a positive number");
          capitalErrorLabel.setVisible(true);
          capitalErrorLabel.setManaged(true);
        }
      }

      private void updateStartButtonState() {
        boolean valid = isNameValid() && isCapitalValid();
        startGameButton.setDisable(!valid);
      }
      //Error display

      private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
      }

      private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

      }
    }



