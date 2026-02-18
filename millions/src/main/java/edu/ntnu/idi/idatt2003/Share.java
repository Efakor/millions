package edu.ntnu.idi.idatt2003;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a share of a stock that a player owns.
 * A share is created when a player purchases a stock, and contains
 * information  about which stock  was bought, how many units,
 * and at what price.
 */


public class Share {
  private final Stock stock;
  private final BigDecimal quantity;
  private final  BigDecimal purchasePrice ;

  /**
   * Creates a new Share with the given stock,quantity and purchase price.
   * @param stock the stock was purchased
   * @param quantity the number of units purchased
   * @param purchasePrice the price per unit at time of purchase
   * @throws IllegalArgumentException if stock is null, or if quantity or purchasePrice is null or not positive
   *
   */

  public Share(Stock stock,BigDecimal quantity,BigDecimal purchasePrice){
    validateStock(stock);
    validateQuantity(quantity);
    validatePurchasePrice(purchasePrice);

    this.stock=stock;
    this.quantity=quantity;
    this.purchasePrice=purchasePrice;


  }

  /**
   * Returns the stock that was purchased.
   * @return the stock
   */
  public Stock getStock() {
    return stock;
  }

  /**
   * Return the quantity of purchased.
   * @return the quantity.
   */
  public BigDecimal getQuantity() {
    return quantity;
  }

  /**
   * Return the price per unit at time of purchase.
   * @return the purchase price
   */
  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }

  /**
   * Validates that the stock is not null.
   * @param stock the stock to validate
   * @throws  IllegalArgumentException if stock is null
   */
  private void validateStock(Stock stock){
    if(stock==null){
      throw new IllegalArgumentException("stock is null");
    }
  }

  /**
   * Validates the quantity is not null, and is not negative.
   * @param quantity the quantity to validate
   * @throws IllegalArgumentException if quality is null or not positive
   */
  private void validateQuantity(BigDecimal quantity){
    if(quantity==null){
      throw new IllegalArgumentException("quantity is null");
    }
    if(quantity.compareTo(BigDecimal.ZERO)<=0){
      throw new IllegalArgumentException("quantity must be greater than zero");
    }
  }

  /**
   * Validates the purchase price is not null and not negative.
   * @param purchasePrice the purchase price  to validate
   * @throws IllegalArgumentException if purchasePrice is null or negative
   */
  private void validatePurchasePrice(BigDecimal purchasePrice){
    if(purchasePrice==null){
      throw new IllegalArgumentException("purchasePrice is null");
    }
    if(purchasePrice.compareTo(BigDecimal.ZERO)<=0){
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