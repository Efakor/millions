package edu.ntnu.idi.idatt2003;

import java.math.BigDecimal;

/**
 * Abstract class representing a financial transaction which involves shares.
 * Transactions can be purchases or sales and are linked to a specific week.
 * Each transaction can only be committed once.
 */
public abstract class Transaction {
    private final Share share;
    private final int week;
    private final TransactionCalculator calculator;
    private boolean committed;

    protected Transaction(Share share, int week, TransactionCalculator calculator) {
        validateShare(share);
        validateWeek(week);
        validateCalculator(calculator);

        this.share = share;
        this.week = week;
        this.calculator = calculator;
        this.committed = false;
    }

    public Share getShare() {
        return share;
    }

    public int getWeek() {
        return week;
    }

    protected TransactionCalculator getCalculator() {
        return calculator;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = true;
    }

    public BigDecimal getGrossValue() {
        return calculator.calculateGross();
    }

    public BigDecimal getCommission() {
        return calculator.calculateCommission();
    }

    public BigDecimal getTax() {
        return calculator.calculateTax();
    }

    public BigDecimal getTotalValue() {
        return calculator.calculateTotal();
    }

    /**
     * Commits this transaction for the given player.
     * This method can only be called once per transaction.
     *
     * @param player the player executing the transaction
     * @throws IllegalStateException if transaction has already been committed
     */
    public abstract void commit(Player player);

    private void validateShare(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
    }

    private void validateWeek(int week) {
        if (week < 1) {
            throw new IllegalArgumentException("Week cannot be less than 1");
        }
    }

    private void validateCalculator(TransactionCalculator calculator) {
        if (calculator == null) {
            throw new IllegalArgumentException("Calculator cannot be null");
        }
    }

}