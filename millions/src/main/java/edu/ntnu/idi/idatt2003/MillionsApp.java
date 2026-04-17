package edu.ntnu.idi.idatt2003;

import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.view.StartScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.logging.Logger;


public class MillionsApp extends Application {
  private static final Logger logger =
      Logger.getLogger(MillionsApp.class.getName());

  @Override
  public void start(Stage stage){
    logger.info("Starting MillionsApp");

    PlayerController playerController=new PlayerController();

    //Creates a start screen
    StartScreen startScreen=new StartScreen(playerController,()->{
      logger.info("The Game has Started!");});
    Scene scene=new Scene(startScreen,800,600);
    stage.setTitle("Millions");
    stage.setScene(scene);
    stage.show();


  }
  public static void main(String[] args) {
    launch(args);
  }


}
