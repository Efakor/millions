package edu.ntnu.idi.idatt2003;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a portfolio of shares owned by a player.
 * A portfolio manages a collection of shares and provides methods to add, remove,
 * and query shares.
 */
public class Portfolio {
    private final List<Share> shares;

    public Portfolio() {
        this.shares = new ArrayList<>();
    }

    public boolean addShare(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        return shares.add(share);
    }

    public boolean removeShare(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        return shares.remove(share);
    }

    public List<Share> getShares() {
        return List.copyOf(shares);
    }

    public List<Share> getShares(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }
        return shares.stream()
                .filter(share -> share.getStock().getSymbol().equals(symbol))
                .collect(Collectors.toList());
    }

    public boolean contains(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        return shares.contains(share);
    }

    public int size() {
        return shares.size();
    }

    public boolean isEmpty() {
        return shares.isEmpty();
    }
}