package edu.ntnu.idi.idatt2003.model;

import java.math.BigDecimal;

/**
 * Represents a player in the stock trading game.
 * Each player has a name, money balance, portfolio of shares, and transaction history.
 * The player's starting capital is preserved to allow for performance calculations.
 *
 * @author efakor
 * @version 1.0
 */
public class Player {
    private final String name;
    private final BigDecimal startingMoney;
    private BigDecimal currentMoney;
    private final Portfolio portfolio;
    private final TransactionArchive transactionArchive;

    /**
     * Constructs a new Player with the given name and starting money.
     * Initializes an empty portfolio and transaction archive.
     *
     * @param name the player's name
     * @param startingMoney the initial amount of money the player has
     * @throws IllegalArgumentException if name is null or empty, or startingMoney is null or non-positive
     */
    public Player(String name, BigDecimal startingMoney) {
        validateName(name);
        validateStartingMoney(startingMoney);

        this.name = name;
        this.startingMoney = startingMoney;
        this.currentMoney = startingMoney;
        this.portfolio = new Portfolio();
        this.transactionArchive = new TransactionArchive();
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's starting money amount.
     * This value never changes and is used to calculate performance.
     *
     * @return the starting money amount
     */
    public BigDecimal getStartingMoney() {
        return startingMoney;
    }

    /**
     * Gets the player's current money balance.
     *
     * @return the current money amount
     */
    public BigDecimal getCurrentMoney() {
        return currentMoney;
    }

    /**
     * Gets the player's portfolio of shares.
     *
     * @return the portfolio
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Gets the player's transaction archive.
     *
     * @return the transaction archive
     */
    public TransactionArchive getTransactionArchive() {
        return transactionArchive;
    }

    /**
     * Validates the player's name.
     *
     * @param name the name to validate
     * @throws IllegalArgumentException if name is null or empty
     */
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name must not be null or empty");
        }
    }

    /**
     * Validates the starting money amount.
     *
     * @param startingMoney the starting money to validate
     * @throws IllegalArgumentException if startingMoney is null or non-positive
     */
    private void validateStartingMoney(BigDecimal startingMoney) {
        if (startingMoney == null) {
            throw new IllegalArgumentException("Player starting money cannot be null");
        }
        if (startingMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Player starting money cannot be negative");
        }
    }

    /**
     * Adds money to the player's balance.
     * Normally used when selling shares.
     *
     * @param amount the amount to add
     * @throws IllegalArgumentException if amount is null or non-positive
     */
    public void addMoney(BigDecimal amount) {
        validateAmount(amount, "Amount to add");
        this.currentMoney = this.currentMoney.add(amount);
    }

    /**
     * Withdraws money from the player's balance.
     * Normally used when purchasing shares.
     *
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if amount is null or non-positive
     * @throws IllegalStateException if player has insufficient funds
     */
    public void withdrawMoney(BigDecimal amount) {
        validateAmount(amount, "Amount to withdraw");

        if (this.currentMoney.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(String.format
                    ("Insufficient funds!! Current amount is: %s, Amount needed: %s", this.currentMoney, amount)
            );
        }
        this.currentMoney = this.currentMoney.subtract(amount);
    }

    /**
     * Validates a money amount.
     *
     * @param amount the amount to validate
     * @param fieldName the name of the field (for error messages)
     * @throws IllegalArgumentException if amount is null or non-positive
     */
    private void validateAmount(BigDecimal amount, String fieldName) {
        if (amount == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }
}