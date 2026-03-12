package edu.ntnu.idi.idatt2003.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a portfolio of shares owned by a player.
 * A portfolio manages a collection of shares, allowing players to track
 * their investments, calculate total value, and manage their holdings.
 *
 * <p>The portfolio supports multiple shares of the same stock (tracked as
 * separate Share objects) and provides methods to retrieve shares by stock
 * symbol or as a complete list.</p>
 *
 * @author efakor
 * @version 1.0
 */
public class Portfolio {
    private final List<Share> shares;

    /**
     * Constructs a new empty Portfolio.
     * A player starts with no shares.
     */
    public Portfolio() {
        this.shares = new ArrayList<>();
    }

    /**
     * Adds a share to the portfolio.
     * Multiple shares of the same stock can be added as separate Share objects.
     *
     * @param share the share to add
     * @return true if the share was added successfully
     * @throws IllegalArgumentException if share is null
     */
    public boolean addShare(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        return shares.add(share);
    }

    /**
     * Removes a share from the portfolio.
     * Removes the first occurrence of the specified share.
     *
     * @param share the share to remove
     * @return true if the share was removed, false if not found
     * @throws IllegalArgumentException if share is null
     */
    public boolean removeShare(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        return shares.remove(share);
    }

    /**
     * Gets all shares in the portfolio.
     *
     * @return unmodifiable list of all shares
     */
    public List<Share> getAllShares() {
        return List.copyOf(shares);
    }

    /**
     * Gets all shares of a specific stock by symbol.
     * Returns all Share objects in the portfolio that match the given stock symbol.
     *
     * @param symbol the stock symbol to search for (e.g., "AAPL")
     * @return list of shares matching the symbol (empty if none found)
     * @throws IllegalArgumentException if symbol is null or empty
     */
    public List<Share> getShares(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }
        return shares.stream()
                .filter(share -> share.getStock().getSymbol().equals(symbol))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the portfolio contains a specific share.
     *
     * @param share the share to check for
     * @return true if the portfolio contains this share, false otherwise
     * @throws IllegalArgumentException if share is null
     */
    public boolean contains(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        return shares.contains(share);
    }

    /**
     * Gets the number of shares in the portfolio.
     * Each Share object counts as one, even if multiple shares
     * are of the same stock.
     *
     * @return the number of shares
     */
    public int size() {
        return shares.size();
    }

    /**
     * Checks if the portfolio is empty.
     *
     * @return true if the portfolio contains no shares, false otherwise
     */
    public boolean isEmpty() {
        return shares.isEmpty();
    }
}