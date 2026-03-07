package edu.ntnu.idi.idatt2003.model;

import edu.ntnu.idi.idatt2003.model.calculator.PurchaseCalculator;

import java.math.BigDecimal;

/**
 * Represents a purchase transaction where a player buys shares.
 * The purchase is demonstrated by committing the transaction,
 * which deducts money from the player's account and adds share to their portfolio.
 *
 */

public class Purchase extends Transaction {
  /**
   * Creates a new Purchase transaction for the given share and week.
   * Automatically creates a PurchaseCalculator to handle cost calculations.
   *
   * @param share the share being purchased
   * @param week the week number when the purchase occurs
   * @throws IllegalArgumentException if share is null or week is invalid
   */
  public Purchase(Share share, int week) {
    super(share, week, new PurchaseCalculator(share));
  }

  /**
   * Commits this purchase transaction for the given player.
   * Deducts the total cost from player's money and adds the share to their portfolio.
   * This method can be called once per transaction.
   *
   * @param player the player making the purchase
   * @throws IllegalStateException if transaction is already committed
   * @throws IllegalStateException if player has insufficient funds
   */

  @Override
  public  void commit(Player player) {
    if (isCommitted()) {
      throw new IllegalStateException("Transaction is already committed");
    }

    BigDecimal totalCost = getCalculator().calculateTotal();
    player.withdrawMoney(totalCost);
    player.getPortfolio().addShare(getShare());
    player.getTransactionArchive().add(this);
    setCommitted(true);

  }
}
