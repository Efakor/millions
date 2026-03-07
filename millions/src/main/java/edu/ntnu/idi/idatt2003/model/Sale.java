package edu.ntnu.idi.idatt2003.model;

import edu.ntnu.idi.idatt2003.model.calculator.TransactionCalculator;

/**
 * Represents a sale transaction where a player sells shares.
 * When committed, the sale adds proceeds to the player's balance,
 * removes the share from the portfolio, and records the transaction.
 *
 * <p>Sales are subject to 1% commission and 30% tax on profits
 * (calculated via SaleCalculator). The player must own the share
 * being sold, and each sale can only be committed once.</p>
 *
 * @author efakor
 * @version 1.0
 */
public class Sale extends Transaction {

    /**
     * Constructs a new Sale transaction.
     * Creates a SaleCalculator internally to handle fee and tax calculations.
     *
     * @param share the share being sold
     * @param week  the week number when the sale occurs
     * @param calculator the
     * @throws IllegalArgumentException if share is null or week is invalid
     */
    public Sale(Share share, int week, TransactionCalculator calculator) {
        super(share, week, calculator);
    }

    /**
     * Commits this sale transaction for the given player.
     *
     * <p>This method:
     * <ol>
     *   <li>Verifies the player owns the share being sold</li>
     *   <li>Verifies the transaction hasn't already been committed</li>
     *   <li>Adds the sale proceeds (after fees and taxes) to the player's money</li>
     *   <li>Removes the share from the player's portfolio</li>
     *   <li>Adds the transaction to the player's archive</li>
     *   <li>Marks the transaction as committed</li>
     * </ol></p>
     *
     * @param player the player executing the sale
     * @throws IllegalArgumentException if player is null
     * @throws IllegalStateException    if transaction is already committed or player doesn't own the share
     */
    @Override
    public void commit(Player player) {
        validatePlayer(player);
        validateNotCommitted();
        validateShareOwnership(player);

        // Add money from sale (gross - commission - tax)
        player.addMoney(getTotalValue());

        // Remove share from portfolio
        player.getPortfolio().removeShare(getShare());

        // Add to transaction archive
        player.getTransactionArchive().add(this);

        // Mark as committed
        setCommitted(true);
    }

    /**
     * Validates that the player is not null.
     *
     * @param player the player to validate
     * @throws IllegalArgumentException if player is null
     */
    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
    }

    /**
     * Validates that the transaction has not already been committed.
     *
     * @throws IllegalStateException if transaction is already committed
     */
    private void validateNotCommitted() {
        if (isCommitted()) {
            throw new IllegalStateException("Transaction has already been committed");
        }
    }

    /**
     * Validates that the player owns the share being sold.
     *
     * @param player the player to check
     * @throws IllegalStateException if player doesn't own the share
     */
    private void validateShareOwnership(Player player) {
        if (!player.getPortfolio().contains(getShare())) {
            throw new IllegalStateException(
                    String.format("Player does not own share: %s", getShare().getStock().getSymbol())
            );
        }
    }
}

