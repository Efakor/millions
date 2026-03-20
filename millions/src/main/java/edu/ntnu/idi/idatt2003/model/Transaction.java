package edu.ntnu.idi.idatt2003.model;

import edu.ntnu.idi.idatt2003.model.calculator.TransactionCalculator;
import java.math.BigDecimal;

/**
 * Abstract class representing a financial transaction which involves shares.
 * Transactions can be purchases or sales and are linked to a specific week.
 * Each transaction can only be committed once.
 *
 * <p>This class uses delegation to handle fee and tax calculations via
 * the TransactionCalculator interface, allowing different calculation
 * strategies for purchases and sales.</p>
 *
 * @author efakor
 * @version 1.0
 */
public abstract class Transaction {
  private final Share share;
  private final int week;
  private final TransactionCalculator calculator;
  private boolean committed;

  /**
   * Constructs a new Transaction.
   * The transaction is initially not committed.
   *
   * @param share      the share involved in this transaction
   * @param week       the week number when this transaction occurred
   * @param calculator the calculator used for computing transaction costs/values
   * @throws IllegalArgumentException if share or calculator is null, or week is less than 1
   */
  protected Transaction(Share share, int week, TransactionCalculator calculator) {
    validateShare(share);
    validateWeek(week);
    validateCalculator(calculator);

    this.share = share;
    this.week = week;
    this.calculator = calculator;
    this.committed = false;
  }

  /**
   * Gets the share involved in this transaction.
   *
   * @return the share
   */
  public Share getShare() {
    return share;
  }

  /**
   * Gets the week number when this transaction occurred.
   *
   * @return the week number
   */
  public int getWeek() {
    return week;
  }

  /**
   * Gets the calculator used for this transaction.
   *
   * @return the transaction calculator
   */
  protected TransactionCalculator getCalculator() {
    return calculator;
  }

  /**
   * Checks if this transaction has been committed.
   * Once committed, a transaction cannot be executed again.
   *
   * @return true if committed, false otherwise
   */
  public boolean isCommitted() {
    return committed;
  }

  /**
   * Sets the committed flag to true.
   * Should only be called by subclass commit() methods after successful execution.
   *
   * @param committed the committed status to set
   */
  public void setCommitted(boolean committed) {
    this.committed = true;
  }

  /**
   * Gets the gross value of the transaction before fees and taxes.
   * Delegates to the calculator.
   *
   * @return the gross value
   */
  public BigDecimal getGrossValue() {
    return calculator.calculateGross();
  }

  /**
   * Gets the commission fee for this transaction.
   * Delegates to the calculator.
   *
   * @return the commission amount
   */
  public BigDecimal getCommission() {
    return calculator.calculateCommission();
  }

  /**
   * Gets the tax for this transaction.
   * Delegates to the calculator.
   *
   * @return the tax amount
   */
  public BigDecimal getTax() {
    return calculator.calculateTax();
  }

  /**
   * Gets the total value after all fees and taxes.
   * For purchases: total cost to pay.
   * For sales: total revenue received.
   * Delegates to the calculator.
   *
   * @return the total value
   */
  public BigDecimal getTotalValue() {
    return calculator.calculateTotal();
  }

  /**
   * Commits this transaction for the given player.
   * This method executes the transaction (withdrawing/adding money,
   * updating portfolio, recording in archive) and can only be called once per transaction.
   *
   * @param player the player executing the transaction
   * @throws IllegalStateException if transaction has already been committed
   */
  public abstract void commit(Player player);

  /**
   * Validates that the share is not null.
   *
   * @param share the share to validate
   * @throws IllegalArgumentException if share is null
   */
  private void validateShare(Share share) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }
  }

  /**
   * Validates that the week is positive.
   *
   * @param week the week to validate
   * @throws java.lang.IllegalArgumentException if week is less than 1
   */
  private void validateWeek(int week) {
    if (week < 1) {
      throw new IllegalArgumentException("Week cannot be less than 1");
    }
  }

  /**
   * Validates that the calculator is not null.
   *
   * @param calculator the calculator to validate
   * @throws IllegalArgumentException if calculator is null
   */
  private void validateCalculator(TransactionCalculator calculator) {
    if (calculator == null) {
      throw new IllegalArgumentException("Calculator cannot be null");
    }
  }

}