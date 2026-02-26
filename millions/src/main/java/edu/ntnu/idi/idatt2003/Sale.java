package edu.ntnu.idi.idatt2003;

public class Sale extends Transaction {

    public Sale(Share share, int week, TransactionCalculator calculator) {
        super(share, week, calculator);
    }

    @Override
    public void commit(Player player) {
        validatePlayer(player);
        validateNotCommitted();
        validateShareOwnership(player);

        // Add money from sale
        player.addMoney(getTotalValue());

        // Remove share from portfolio
        player.getPortfolio().removeShare(getShare());

        // Add to transaction archive
        player.getTransactionArchive().add(this);

        // Mark as committed
        setCommitted(true);
    }

    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
    }

    private void validateNotCommitted() {
        if (isCommitted()) {
            throw new IllegalStateException("Transaction has already been committed");
        }
    }

    private void validateShareOwnership(Player player) {
        if (!player.getPortfolio().contains(getShare())) {
            throw new IllegalStateException(
                    String.format("Share owner does not exist: %s", getShare().getStock().getSymbol())
            );
        }
    }
}
