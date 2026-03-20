package edu.ntnu.idi.idatt2003.model.calculator;

import edu.ntnu.idi.idatt2003.model.Share;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculator for purchase transactions.
 * Calculates costs and fess when buying shares,including commission.
 */

public class PurchaseCalculator implements TransactionCalculator {
  private final BigDecimal purchasePrice;
  private final  BigDecimal quantity;

  /**
   * Creates a new PurchaseCalculator for the given share.
   *
   * @param share the share being purchased
   * @throws IllegalArgumentException if share is null.
   */
  public  PurchaseCalculator(Share share) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");

    }
    this.purchasePrice = share.purchasePrice();
    this.quantity = share.quantity();

  }

  /**
   * Calculates the gross value.
   *
   * @return the gross value before fees and rounded by 2 decimals.
   */
  @Override
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Calculates the commission fee for the purchase.
   * The commission is 0.5 % for the gross purchase
   *
   * @return the commission fee, rounded by 2 decimals
   */
  @Override
  public BigDecimal calculateCommission() {
    BigDecimal rate = new BigDecimal("0.005"); // 0.5%
    return calculateGross().multiply(rate).setScale(2, RoundingMode.HALF_UP);

  }

  /**
   * Calculates tax on the purchase.
   * No tax is charged in purchasing shares.
   *
   * @return zero, since no tox is charged when buying shares
   */
  @Override
  public BigDecimal calculateTax() {
    return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Calculates the total cost of the purchase .
   * Total is calculated gross+commission+tax.
   *
   * @return the total cost after all fees the answer is rounded to 2 decimals
   */
  @Override
  public BigDecimal calculateTotal() {
    return calculateGross()
        .add(calculateCommission())
        .add(calculateTax())
        .setScale(2, RoundingMode.HALF_UP);
  }
}
