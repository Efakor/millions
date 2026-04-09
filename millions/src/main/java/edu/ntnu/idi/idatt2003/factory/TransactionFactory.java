package edu.ntnu.idi.idatt2003.factory;
import edu.ntnu.idi.idatt2003.model.Purchase;
import edu.ntnu.idi.idatt2003.model.Sale;
import edu.ntnu.idi.idatt2003.model.Share;
import edu.ntnu.idi.idatt2003.model.calculator.PurchaseCalculator;
import edu.ntnu.idi.idatt2003.model.calculator.SaleCalculator;
import edu.ntnu.idi.idatt2003.model.calculator.TransactionCalculator;

public class TransactionFactory {
  /**
   * Creates purchase class
   */

  public static Purchase createPurchase(Share share, int week) {
    TransactionCalculator calculator=new PurchaseCalculator(share);
    return new Purchase(share, week,calculator);
  }

  public static Sale createSale(Share share, int week) {
    TransactionCalculator calculator=new SaleCalculator(share);
    return new Sale(share,week,calculator);
  }
}
