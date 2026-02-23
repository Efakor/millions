package edu.ntnu.idi.idatt2003;
import java.math.BigDecimal;

/**
 * Interface defining calculations for financial transactions.
 * Implementations handle tax,commission and total value calculations
 * for different transaction types.(purchases and sales)
 */

public interface TransactionCalculator {
  BigDecimal calculateGross();
  BigDecimal calculateCommission();
  BigDecimal calculateTax();
  BigDecimal calculateTotal();


}
