package edu.ntnu.idi.idatt2003;

import edu.ntnu.idi.idatt2003.view.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Millions");
    primaryStage.setMinWidth(900);
    primaryStage.setMinHeight(600);

    StartView startView = new StartView(primaryStage);
    Scene scene = new Scene(startView, 900, 600);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  static void main(String[] args) {
    launch(args);
  }
}
