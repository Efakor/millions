package edu.ntnu.idi.idatt2003;

import java.math.BigDecimal;
import java.util.Objects;


public class Share {
  private final Stock stock;
  private final BigDecimal quantity;
  private final  BigDecimal purchasePrice ;

  public Share(Stock stock,BigDecimal quantity,BigDecimal purchasePrice){
    validateStock(stock);
    validateQuantity(quantity);
    validatePurchasePrice(purchasePrice);

    this.stock=stock;
    this.quantity=quantity;
    this.purchasePrice=purchasePrice;


  }
  public Stock getStock() {
    return stock;
  }
  public BigDecimal getQuantity() {
    return quantity;
  }
  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }
  private void validateStock(Stock stock){
    if(stock==null){
      throw new IllegalArgumentException("stock is null");
    }
  }
  private void validateQuantity(BigDecimal quantity){
    if(quantity==null){
      throw new IllegalArgumentException("quantity is null");
    }
    if(quantity.compareTo(BigDecimal.ZERO)<=0){
      throw new IllegalArgumentException("quantity must be greater than zero");
    }
  }
  private void validatePurchasePrice(BigDecimal purchasePrice){
    if(purchasePrice==null){
      throw new IllegalArgumentException("purchasePrice is null");
    }if(purchasePrice.compareTo(BigDecimal.ZERO)<=0){
      throw new IllegalArgumentException("purchasePrice must be greater than zero");
    }
  }
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (o == null || getClass() != o.getClass())
      return false;
    Share share = (Share) o;
    return Objects.equals(stock, share.stock) && Objects.equals(purchasePrice, share.purchasePrice);
  }
  @Override
  public int hashCode() {
    return Objects.hash(stock,quantity, purchasePrice);
  }
  @Override
  public String toString(){
    return String.format("Stock: %s, Quantity: %s, PurchasePrice: %s",
        stock.getSymbol(), quantity, purchasePrice);
  }


}