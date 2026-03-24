package edu.ntnu.idi.idatt2003.model.calculator;

import java.math.BigDecimal;

/**
 * Interface defining calculations for financial transactions.
 * Implementations handle tax,commission and total value calculations
 * for different transaction types.(purchases and sales)
 */

public interface TransactionCalculator {
  /**
   * .
   *
   * @return .
   */
  BigDecimal calculateGross();

  /**
   * .
   *
   * @return .
   */
  BigDecimal calculateCommission();

  /**
   * .
   *
   * @return .
   */
  BigDecimal calculateTax();

  /**
   * .
   *
   * @return .
   */
  BigDecimal calculateTotal();
}
