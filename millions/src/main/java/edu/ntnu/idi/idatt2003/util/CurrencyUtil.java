package edu.ntnu.idi.idatt2003.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting currency values in NOK.
 */
public class CurrencyUtil {

  private static final Locale NORWAY = Locale.forLanguageTag("nb-NO");

  private CurrencyUtil() {}

  /**
   * Formats a BigDecimal as Norwegian kroner.
   *
   * @param amount the amount to format
   * @return formatted string e.g. "kr 10 000,00"
   */
  public static String formatNok(BigDecimal amount) {
    NumberFormat nf = NumberFormat.getCurrencyInstance(NORWAY);
    return nf.format(amount);
  }
}
