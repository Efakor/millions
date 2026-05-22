package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;


public class NewsFeedPanel extends VBox implements GameObserver {
  private static final String STYLE_BACKGROUND = "-fx-background-color: #0D1117;";
  private static final String SET_TITLE = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: monospace; -fx-font-size: 11px;-fx-font-weight: bold;";
  private static final String STYLE_PANEL = "-fx-background-color: #161B22;"
      + " -fx-border-color: #30363D; -fx-border-width: 1;";
  private static final String STYLE_LABEL = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: monospace; -fx-font-size: 11px;";
  private static final String STYLE_VALUE = "-fx-text-fill: #E6EDF3;"
      + " -fx-font-family: monospace; -fx-font-size: 12px;";
  private static final String STYLE_HEADLINE_GREEN = "-fx-text-fill: #3FB950;" +
      "-fx-font-family: monospace; -fx-font-size: 11px;-fx-font-weight: bold;";
  private static final String STYLE_HEADLINE_RED = "-fx-text-fill: #F85149;" +
      "-fx-font-family: monospace; -fx-font-size: 11px;-fx-font-weight: bold;";
  private static final String STYLE_WEEK = "-fx-text-fill: #8B949E;" +
      "-fx-font-family: monospace; -fx-font-size: 10px;";

  private final ExchangeController exchangeController;
  private ScrollPane scrollPane;
  private VBox headlinesList;

  public NewsFeedPanel(ExchangeController exchangeController) {
    this.exchangeController = exchangeController;
    this.headlinesList = new VBox(5);
    buildUi();
    exchangeController.getExchange().addObserver(this);

  }

  private void buildUi() {
    Label title = new Label("MARKET NEWS");
    title.setStyle(SET_TITLE);
    headlinesList.setStyle(STYLE_BACKGROUND);

    ScrollPane scrollPane = new ScrollPane(headlinesList);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(200);
    scrollPane.setStyle(STYLE_BACKGROUND);
    setStyle(STYLE_PANEL);
    setPadding(new Insets(10));
    getChildren().addAll(title, scrollPane);
  }

  @Override
  public void onGameEvent(GameEvent event) {
    if (event == GameEvent.WEEK_ADVANCED) {
      List<String> headlines = exchangeController.getExchange().getHeadlines();

      if (headlines.size() == 0) {
        // Get latest headlin(last in the list)
        String latest = headlines.get(headlines.size() - 1);
        //Week label
        Label weekLabel = new Label("WEEK" + exchangeController.getExchange().getWeek());
        weekLabel.setStyle(STYLE_WEEK);
        Label headlineLabel = new Label(latest);
        if (latest.contains("surges")) {
          headlineLabel.setStyle(STYLE_HEADLINE_GREEN);
        } else {
          headlineLabel.setStyle(STYLE_HEADLINE_RED);
        }
        VBox vBox = new VBox(2, weekLabel, headlineLabel);
        vBox.setStyle(STYLE_PANEL);
        vBox.setPadding(new Insets(10));
        headlinesList.getChildren().add(0, vBox);

      }
    }
  }
}
