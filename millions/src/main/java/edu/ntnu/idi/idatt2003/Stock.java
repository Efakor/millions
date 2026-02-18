package edu.ntnu.idi.idatt2003;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stock {
  private final String symbol;
  private final String company;
  private final List<BigDecimal> prices;

  /**
   * Creates a new Stock with the given symbol,company name, and initial sales price.
   * @param symbol the unique stock symbol(e.g,"AAPL")
   * @param company the company name (e.g,"Apple INC")
   * @param salesPrice the initial sales price of the stock
   * @throws IllegalArgumentException if symbol or company is null or empty,
   *                                  or if salesPrice is null or not positive
   */

  public Stock(String symbol, String company,BigDecimal salesPrice) {
    validateSymbol(symbol);
    validateCompany(company);
    validateSalesPrice(salesPrice);
    this.symbol = symbol;
    this.company = company;
    this.prices = new ArrayList<>();
    this.prices.add(salesPrice);


}

  /**
   * Returns the stock symbol.
   * @return the stock symbol
   */
  public String getSymbol() {
    return symbol;
}

  /**
   * Returns the company name.
   * @return the company name.
   */
  public String getCompany() {
    return company;
}

  /**
   * Returns the current sales price of the stock.
   * This is the most recently added price in the price history.
   * @return the current sales price
   */

public BigDecimal getSalesPrice() {
    return prices.get(prices.size()-1);
}

  /**
   * Adds a new sales price to the stock's price history.
   * This is typically calls when the market updates prices weekly.
   * @param newPrice the new sales price to add
   * @throws IllegalArgumentException if price is null or not positive
   *
   */
  public void addNewSalesPrice(BigDecimal newPrice) {
    validateSalesPrice(newPrice);
    prices.add(newPrice);
}

  /**
   * Returns an unmodifiable view of the price history.
   * This can be useful for analyzing price trends.
   * @return a list containing all historical prices
   */
  public List<BigDecimal> getPriceHistory() {
    return new  ArrayList<>(prices);

}

  /**
   * Validates that the symbol is not null or empty.
   * @param salesPrice the sales price to validate
   * @throws IllegalArgumentException if salePrice is null or not positive
   */
  private void validateSalesPrice(BigDecimal salesPrice) {
    if (salesPrice == null) {
      throw new IllegalArgumentException("Sales price cannot be null");
    }
    if (salesPrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Sales Price must be positive");
    }

  }

  /**
   * Validates that the company name is not null or empty.
   * @param company the company name to validate
   * @throws IllegalArgumentException if company is null or empty
   */
  private void validateCompany(String company) {
    if (company == null|| company.trim().isEmpty()) {
      throw new IllegalArgumentException("Company name cannot be empty or null.");
    }

  }

  /**
   * Validates that the sales price is not null or empty.
   * @param symbol the symbol to validate
   * @throws IllegalArgumentException if symbol is null or empty
   */
  private void validateSymbol(String symbol) {
    if (symbol == null|| symbol.trim().isEmpty()) {
      throw new IllegalArgumentException("Stock symbol cannot be empty or null.");
    }

  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Stock stock = (Stock) o;
    return Objects.equals(symbol, stock.symbol);
  }@Override
  public int hashCode() {
    return Objects.hash(symbol);
  }
  @Override
  public String toString() {
    return String.format("%s - %s (Current Price: %s)",
        symbol, company, getSalesPrice());
  }



}
