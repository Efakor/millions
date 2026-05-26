package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


/**
 * Panel displaying market event headlines generated during week advancement.
 * Implements {@link GameObserver} to automatically append new headlines
 * after each week advance. Headlines are colour-coded green for surges
 * and red for crashes.
 */


public class NewsFeedPanel extends VBox implements GameObserver {

  private static final String STYLE_BACKGROUND = "-fx-background-color: #0D1117;";
  private static final String SET_TITLE = "-fx-text-fill: #8B949E;"
      + " -fx-font-family: monospace; -fx-font-size: 11px;-fx-font-weight: bold;";
  private static final String STYLE_PANEL = "-fx-background-color: #161B22;"
      + " -fx-border-color: #30363D; -fx-border-width: 1;";
  private static final String STYLE_SCROLLPANE = "-fx-background-color: #0D1117;"
      + "-fx-background: #0D1117; -fx-viewport-background-color: #0D1117;";
  private static final String STYLE_HEADLINE_GREEN = "-fx-text-fill: #3FB950;"
      + "-fx-font-family: monospace; -fx-font-size: 11px;-fx-font-weight: bold;";
  private static final String STYLE_HEADLINE_RED = "-fx-text-fill: #F85149;"
      + "-fx-font-family: monospace; -fx-font-size: 11px;-fx-font-weight: bold;";
  private static final String STYLE_WEEK = "-fx-text-fill: #8B949E;"
      + "-fx-font-family: monospace; -fx-font-size: 10px;";

  private final ExchangeController exchangeController;
  private ScrollPane scrollPane;
  private VBox headlinesList;

  /**
   * Construct NewsFeedPanel and registers it as a GameObserver
   * Displays headlines list and builds the UI.
   *
   * @param exchangeController the exchange controller used to fetch headlines.
   */

  public NewsFeedPanel(ExchangeController exchangeController) {
    this.exchangeController = exchangeController;
    this.headlinesList = new VBox(5);
    buildUi();
    exchangeController.getExchange().addObserver(this);

  }

  /**
   * Builds the UI panel, including the title label and scrollable headlines.
   */

  private void buildUi() {
    Label title = new Label("MARKET NEWS");
    title.setStyle(SET_TITLE);
    headlinesList.setStyle(STYLE_BACKGROUND);

    ScrollPane scrollPane = new ScrollPane(headlinesList);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(200);
    scrollPane.setStyle(STYLE_SCROLLPANE);
    setStyle(STYLE_PANEL);
    setPadding(new Insets(10));
    getChildren().addAll(title, scrollPane);
  }

  /**
   *Panel displaying market event headlines generated during week advancement.
   *Implements {@link GameObserver} to automatically append new headlines
   *after each week advance. Headlines are colour-coded green for surges
   * and red for crashes.
   *
   * @param event the event that was triggered
   */

  @Override
  public void onGameEvent(GameEvent event) {
    if (event == GameEvent.WEEK_ADVANCED) {
      List<String> headlines = exchangeController.getExchange().getHeadlines();

      if (!headlines.isEmpty()) {
        // Get latest headlin(last in the list)
        String latest = headlines.get(headlines.size() - 1);
        // Week label
        Label weekLabel = new Label("WEEK" + exchangeController.getExchange().getWeek());
        weekLabel.setStyle(STYLE_WEEK);
        Label headlineLabel = new Label(latest);
        if (latest.contains("surges")) {
          headlineLabel.setStyle(STYLE_HEADLINE_GREEN);
        } else {
          headlineLabel.setStyle(STYLE_HEADLINE_RED);
        }
        VBox box = new VBox(2, weekLabel, headlineLabel);
        box.setStyle(STYLE_SCROLLPANE);
        box.setPadding(new Insets(10));
        headlinesList.getChildren().add(0, box);

      }
    }
  }
}
