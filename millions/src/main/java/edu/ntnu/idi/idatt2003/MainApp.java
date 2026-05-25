package edu.ntnu.idi.idatt2003;

import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.view.StartView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main entry point for the Millions stock trading game.
 * Initialises the JavaFX application, creates the primary stage
 * and launches the start screen.
 */

public class MainApp extends Application {
  /**
   * Starts the JavaFX application by configuring the primary stage
   * and displaying the start screen.
   *
   * @param primaryStage the primary stage provided by the JavaFX runtime
   */

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

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args command line arguments passed to the application
   */

  public static void main(String[] args) {
    launch(args);
  }
}
