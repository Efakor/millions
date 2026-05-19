package edu.ntnu.idi.idatt2003;

import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.view.StartView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Millions");
    primaryStage.setMinWidth(900);
    primaryStage.setMinHeight(600);

    PlayerController playerController = new PlayerController();
    StartView startView = new StartView(playerController, primaryStage);
    Scene scene = new Scene(startView, 1200, 750);

    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
