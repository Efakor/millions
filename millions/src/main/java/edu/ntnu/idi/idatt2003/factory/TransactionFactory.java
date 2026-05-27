package edu.ntnu.idi.idatt2003.factory;

import edu.ntnu.idi.idatt2003.model.Purchase;
import edu.ntnu.idi.idatt2003.model.Sale;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.calculator.PurchaseCalculator;
import edu.ntnu.idi.idatt2003.model.calculator.SaleCalculator;
import edu.ntnu.idi.idatt2003.model.calculator.TransactionCalculator;

/**
 * Factory class for creating transaction objects.
 * Centralizes the construction of Purchase and Sale instances so that
 * no other class needs to call their constructors directly.
 */
public class TransactionFactory {

  /**
   * Creates a new Purchase transaction for the given share and week.
   * Wires a PurchaseCalculator to the transaction automatically.
   *
   * @param share the share being purchased
   * @param week  the current trading week
   * @return a new Purchase ready to be committed
   */
  public static Purchase createPurchase(Share share, int week) {
    TransactionCalculator calculator = new PurchaseCalculator(share);
    return new Purchase(share, week, calculator);
  }

  /**
   * Creates a new Sale transaction for the given share and week.
   * Wires a SaleCalculator to the transaction automatically.
   *
   * @param share the share being sold
   * @param week  the current trading week
   * @return a new Sale ready to be committed
   */
  public static Sale createSale(Share share, int week) {
    TransactionCalculator calculator = new SaleCalculator(share);
    return new Sale(share, week, calculator);
  }
}
