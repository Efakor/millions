package edu.ntnu.idi.idatt2003.factory;

import edu.ntnu.idi.idatt2003.model.Purchase;
import edu.ntnu.idi.idatt2003.model.Sale;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.calculator.PurchaseCalculator;
import edu.ntnu.idi.idatt2003.model.calculator.SaleCalculator;
import edu.ntnu.idi.idatt2003.model.calculator.TransactionCalculator;

/**
 * Factory class responsible for creating transaction objects.
 * Centralises the instantiation of {@link Purchase} and {@link Sale} objects,
 * ensuring that each transaction is always paired with the correct calculator.
 */

public class TransactionFactory {

  /**
   * Creates a new purchase transaction for the given share and week.
   * Automatically wires the transaction with a {@link PurchaseCalculator}
   * that applies 0.5% commission and no capital gains tax.
   *
   * @param share the share being purchased
   * @param week  the week number when the purchase is executed
   * @return a new uncommitted {@link Purchase} transaction
   * @throws IllegalArgumentException if share is null or week is less than 1
   */


  public static Purchase createPurchase(Share share, int week) {
    TransactionCalculator calculator = new PurchaseCalculator(share);
    return new Purchase(share, week, calculator);
  }

  /**
   * Creates a new sale transaction for the given share and week.
   * Wires the transaction with a {@link SaleCalculator}
   * that applies 1% commission and 30% capital gains tax on any profit.
   *
   * @param share the share being sold
   * @param week  the week number when the sale is executed
   * @return a new uncommitted {@link Sale} transaction
   * @throws IllegalArgumentException if share is null or week is less than 1
   */

  public static Sale createSale(Share share, int week) {
    TransactionCalculator calculator = new SaleCalculator(share);
    return new Sale(share, week, calculator);
  }
}
