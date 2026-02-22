package edu.ntnu.idi.idatt2003;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesCalculator implements TransactionCalculator{
  private final BigDecimal purchasePrice;
  private final BigDecimal salePrice;
  private final BigDecimal quantity;
  public SalesCalculator(Share share) {
    if (share==null){
      throw new NullPointerException("Share cannot be null");
    }
    this.purchasePrice = share.getPurchasePrice();
    this.salePrice = share.getStock().getSalesPrice();
    this.quantity=share.getQuantity();
  }
  @Override
  public BigDecimal calculateGross(){
    return salePrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);

  }
  @Override
  public BigDecimal calculateCommission(){
    BigDecimal commission =new BigDecimal("0.01"); //1%

    return calculateGross().multiply(commission).setScale(2, RoundingMode.HALF_UP);
  }
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
  @Override
  public BigDecimal calculateTotal(){
    return calculateGross().
        subtract(calculateCommission()).
        subtract(calculateTax()).
        setScale(2, RoundingMode.HALF_UP);
  }


}
