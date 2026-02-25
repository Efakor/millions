package edu.ntnu.idi.idatt2003;
/**
 * Calculator for purchase transactions.
 * Calculates costs and fees when buying shares.
 */

import java.math.BigDecimal;
import java.math.RoundingMode;




public class PurchaseCalculator implements TransactionCalculator{
  private final BigDecimal purchasePrice;
  private final  BigDecimal quantity;
  public  PurchaseCalculator(Share share) {
    if(share==null){
      throw new IllegalArgumentException("Share cannot be null");

    }
    this.purchasePrice=share.getPurchasePrice();
    this.quantity=share.getQuantity();

  }
@Override
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
  }
  @Override
  public BigDecimal calculateCommission() {
    BigDecimal rate =new BigDecimal("0.005"); //0.5%
    return calculateGross().multiply(rate).setScale(2, RoundingMode.HALF_UP);

  }
  @Override
  public BigDecimal calculateTax() {
    return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
  }
  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().add(calculateCommission()).add(calculateTax()).setScale(2, RoundingMode.HALF_UP);
  }


}
