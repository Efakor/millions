package edu.ntnu.idi.idatt2003;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculator for sale transactions.
 * Calculates proceeds and costs selling shares,as well as commission and profit-base tax.
 */

public class SaleCalculator implements TransactionCalculator{
  private final BigDecimal purchasePrice;
  private final BigDecimal salePrice;
  private final BigDecimal quantity;

  /**
   * Creates a new SaleCalculator for the given share.
   * Gets the purchase price, current sale price, and quantity from the share.
   * @param share the share being sold
   * @throws  IllegalArgumentException if share is null
   */
  public SaleCalculator(Share share) {
    if (share==null){
      throw new IllegalArgumentException("Share cannot be null");
    }
    this.purchasePrice = share.getPurchasePrice();
    this.salePrice = share.getStock().getSalesPrice();
    this.quantity=share.getQuantity();
  }

  /**
   * Calculates gross value of the sale (salePrice*quantity)
   * @return  the gross value before commission and tex, rounded to 2 decimals-
   */
  @Override
  public BigDecimal calculateGross(){
    return salePrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);

  }

  /**
   * Calculates the commission fee for the sale.
   * The commission is 1% of the set value
   * @return the commission fee, rounded 2 decimal places
   */
  @Override
  public BigDecimal calculateCommission(){
    BigDecimal commission =new BigDecimal("0.01"); //1%

    return calculateGross().multiply(commission).setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Calculates tax for the sale.
   * Tax is 30% of the profit, it's calculated as
   * gross value-commission- original price.
   * @return the tax amount, rounded by 2 decimals
   */
  @Override
  public BigDecimal calculateTax(){
    BigDecimal tax =new BigDecimal("0.3");//30%
    BigDecimal profit= calculateGross().
        subtract(calculateCommission()).
        subtract((purchasePrice.multiply(quantity))).
        setScale(2, RoundingMode.HALF_UP);
    if(profit.compareTo(BigDecimal.ZERO)<=0){
      return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
    return tax.multiply(profit).setScale(2, RoundingMode.HALF_UP);



  }

  /**
   * Calculates total from the sale.
   * Total is calculated as: gross value-commission-tax.
   * This what the actual amount the seller recieves
   * @return the total amount, rounded to 2 decimals
   */
  @Override
  public BigDecimal calculateTotal(){
    return calculateGross().
        subtract(calculateCommission()).
        subtract(calculateTax()).
        setScale(2, RoundingMode.HALF_UP);
  }


}
