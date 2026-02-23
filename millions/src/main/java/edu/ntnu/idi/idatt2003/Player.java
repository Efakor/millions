package edu.ntnu.idi.idatt2003;

import java.math.BigDecimal;

/**
 * Represents a player in the stock trading game.
 * Each player has a name, money balance, portfolio of shares, and transaction history.
 */
public class Player {
    private final String name;
    private final BigDecimal startingMoney;
    private BigDecimal currentMoney;
    private final Portfolio portfolio;
    private final TransactionArchive transactionArchive;

    public Player(String name, BigDecimal startingMoney) {
        validateName(name);
        validateStartingMoney(startingMoney);

        this.name = name;
        this.startingMoney = startingMoney;
        this.currentMoney = startingMoney;
        this.portfolio = new Portfolio();
        this.transactionArchive = new TransactionArchive();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getStartingMoney() {
        return startingMoney;
    }

    public BigDecimal getCurrentMoney() {
        return currentMoney;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public TransactionArchive getTransactionArchive() {
        return transactionArchive;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name must not be null or empty");
        }
    }

    private void validateStartingMoney(BigDecimal startingMoney) {
        if (startingMoney == null) {
            throw new IllegalArgumentException("Player starting money cannot be null");
        }
        if (startingMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Player starting money cannot be negative");
        }
    }

    public void addMoney(BigDecimal amount) {
        validateAmount(amount, "Amount to add");
        this.currentMoney = this.currentMoney.add(amount);
    }

    public void withdrawMoney(BigDecimal amount) {
        validateAmount(amount, "Amount to withdraw");

        if (this.currentMoney.subtract(amount).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException(String.format
                    ("Insufficient funds!! Current amount is: %s, Amount needed: %s", this.currentMoney, amount)
            );
        }
        this.currentMoney = this.currentMoney.subtract(amount);
    }

    private void validateAmount(BigDecimal amount, String fieldName) {
        if (amount == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }
}