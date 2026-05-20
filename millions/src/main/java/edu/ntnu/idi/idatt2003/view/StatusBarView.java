package edu.ntnu.idi.idatt2003.view;

import edu.ntnu.idi.idatt2003.controller.ExchangeController;
import edu.ntnu.idi.idatt2003.controller.PlayerController;
import edu.ntnu.idi.idatt2003.observer.GameEvent;
import edu.ntnu.idi.idatt2003.observer.GameObserver;
import edu.ntnu.idi.idatt2003.util.CurrencyUtil;
import java.math.BigDecimal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The persistent bottom bar showing net worth, P&L, weeks played, and status.
 * Registers as a GameObserver and refreshes on every game event.
 * Never requires a manual refresh call from outside this class.
 */
public class StatusBarView extends HBox implements GameObserver {

  private final PlayerController playerController;
  private final ExchangeController exchangeController;

  private final Label netWorthLabel = new Label(CurrencyUtil.formatNok(BigDecimal.ZERO));
  private final Label pnlLabel = new Label(CurrencyUtil.formatNok(BigDecimal.ZERO));
  private final Label weeksLabel = new Label("0");
  private final Label statusLabel = new Label("NOVICE");

  /**
   * Constructs the status bar.
   *
   * @param playerController the player controller
   */
  public StatusBarView(PlayerController playerController, ExchangeController exchangeController) {
    this.playerController = playerController;
    this.exchangeController = exchangeController;
    buildLayout();
  }

  private void buildLayout() {
    setSpacing(24);
    setPadding(new Insets(8, 14, 8, 14));
    setAlignment(Pos.CENTER_LEFT);
    setStyle(
        "-fx-background-color: #161B22;"
        + "-fx-border-color: #21262D;"
        + "-fx-border-width: 1 0 0 0;"
    );

    applyValueStyle(netWorthLabel);
    applyValueStyle(pnlLabel);
    applyValueStyle(weeksLabel);

    statusLabel.setStyle(
        "-fx-background-color: #1A2D1A;"
        + "-fx-text-fill: #3FB950;"
        + "-fx-padding: 2 10 2 10;"
        + "-fx-background-radius: 12;"
        + "-fx-font-family: monospace;"
        + "-fx-font-weight: bold;"
        + "-fx-font-size: 11px;"
    );

    getChildren().addAll(
        buildItem("NET WORTH", netWorthLabel),
        buildItem("P&L", pnlLabel),
        buildItem("WEEKS", weeksLabel),
        buildItem("STATUS", statusLabel)
    );
  }

  private void applyValueStyle(Label label) {
    label.setStyle(
        "-fx-text-fill: #E6EDF3;"
        + "-fx-font-weight: bold;" + "-fx-font-size: 13px;"
        + "-fx-font-family: monospace;");
  }

  private VBox buildItem(String title, Label valueLabel) {
    Label titleLabel = new Label(title);
    titleLabel.setStyle(
        "-fx-font-family: monospace;"
        + "-fx-font-size: 8px;"
        + "-fx-text-fill: #8B949E;");
    VBox box = new VBox(1, titleLabel, valueLabel);
    box.setAlignment(Pos.CENTER_LEFT);
    return box;
  }

  /**
   * Called by Exchange whenever a game event fires.
   * Refreshes all displayed values automatically.
   *
   * @param event the event that was fired
   */
  @Override
  public void onGameEvent(GameEvent event) {
    refresh();
  }

  private void refresh() {
    var player = playerController.getPlayer();
    if (player == null) {
      return;
    }

    BigDecimal netWorth = player.getNetWorth();
    BigDecimal pnl = netWorth.subtract(player.getStartingMoney());

    netWorthLabel.setText(CurrencyUtil.formatNok(netWorth));

    weeksLabel.setText(String.valueOf(
        exchangeController.getExchange().getWeek()));

    String pnlText = (pnl.signum() >= 0 ? "+" : "-") + CurrencyUtil.formatNok(pnl.abs());
    pnlLabel.setText(pnlText);
    pnlLabel.setStyle(pnlLabel.getStyle()
        .replaceAll("-fx-text-fill: #[0-9A-Fa-f]+;",
            pnl.signum() >= 0
                ? "-fx-text-fill: #3FB950;"
                : "-fx-text-fill: #F85149;"
            ));

    String status = player.getStatus();
    statusLabel.setText(status);
    
    String badgeBackground = switch (status) {
      case "INVESTOR" -> "#1A2D5F";
      case "SPECULATOR" -> "#3A2A00";
      default -> "#1A2D1A";
    };
    String badgeText = switch (status) {
      case "INVESTOR" -> "#79C0FF";
      case "SPECULATOR" -> "#F8CC1B";
      default -> "#3FB950";
    };
    statusLabel.setStyle(
        "-fx-font-family: monospace;"
        + "-fx-font-size: 11px;"
        + "-fx-text-fill: " + badgeText + ";"
        + "-fx-padding: 2 10 2 10;"
        + "-fx-background-radius: 12;"
        + "-fx-font-weight: bold;"
        + "-fx-background-color: " + badgeBackground + ";"
    );
  }
}
