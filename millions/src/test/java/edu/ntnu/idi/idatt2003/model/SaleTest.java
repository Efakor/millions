package edu.ntnu.idi.idatt2003.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt2003.model.calculator.SaleCalculator;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Sale class.
 */
class SaleTest {
  private Stock stock;
  private Share share;
  private Player player;
  private SaleCalculator calculator;
  private Sale sale;

  @BeforeEach
  void setUp() {
    stock = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
    share = new Share(stock, new BigDecimal("10"), new BigDecimal("140.00"));
    player = new Player("Test Player", new BigDecimal("10000.00"));

    // Player must own the share to sell it
    player.getPortfolio().addShare(share);

    // Sale at $160 (profit: 160 - 140 = 20 per share, total profit = 200)
    stock.addNewSalesPrice(new BigDecimal("160.00"));
    calculator = new SaleCalculator(share);
    sale = new Sale(share, 1, calculator);
  }

  @Test
  void constructorWithValidParameters() {
    assertNotNull(sale);
    assertEquals(share, sale.getShare());
    assertEquals(1, sale.getWeek());
    assertFalse(sale.isCommitted());
  }

  @Test
  void constructorWithNullShare() {
    assertThrows(IllegalArgumentException.class, () -> new Sale(null, 1, calculator));
  }

  @Test
  void constructorWithNullCalculator() {
    assertThrows(IllegalArgumentException.class, () -> new Sale(share, 1, null));
  }

  @Test
  void constructorWithInvalidWeek() {
    assertThrows(IllegalArgumentException.class, () -> new Sale(share, 0, calculator));
  }

  @Test
  void commitWithValidSaleAddsMoney() {
    BigDecimal initialMoney = player.getCurrentMoney();
    sale.commit(player);
    BigDecimal expected = initialMoney.add(sale.getTotalValue());
    assertEquals(expected, player.getCurrentMoney());
  }

  @Test
  void commitWithValidSaleRemovesShareFromPortfolio() {
    assertTrue(player.getPortfolio().contains(share));
    sale.commit(player);
    assertFalse(player.getPortfolio().contains(share));
  }

  @Test
  void commitWithValidSaleAddsToArchive() {
    assertTrue(player.getTransactionArchive().isEmpty());
    sale.commit(player);
    assertFalse(player.getTransactionArchive().isEmpty());
    assertEquals(1, player.getTransactionArchive().getAllTransactions().size());
    assertTrue(player.getTransactionArchive().getAllTransactions().contains(sale));
  }

  @Test
  void commitWithValidSaleMarksAsCommitted() {
    assertFalse(sale.isCommitted());
    sale.commit(player);
    assertTrue(sale.isCommitted());
  }

  @Test
  void commitWithNullPlayer() {
    assertThrows(IllegalArgumentException.class, () -> sale.commit(null));
  }

  @Test
  void commitWhenAlreadyCommitted() {
    sale.commit(player);
    assertThrows(IllegalStateException.class, () -> sale.commit(player));
  }

  @Test
  void commitWhenPlayerDoesNotOwnShare() {
    // Remove share from portfolio
    player.getPortfolio().removeShare(share);
    IllegalStateException exception = assertThrows(
        IllegalStateException.class, () -> sale.commit(player));
    assertTrue(exception.getMessage().contains("does not own share"));
  }

  @Test
  void commitCompletesFullSaleWorkflow() {
    BigDecimal initialMoney = player.getCurrentMoney();
    int initialPortfolioSize = player.getPortfolio().size();
    sale.commit(player);

    // Money increased
    assertTrue(player.getCurrentMoney().compareTo(initialMoney) > 0);

    // Portfolio decreased
    assertEquals(initialPortfolioSize - 1, player.getPortfolio().size());

    // Transaction recorded
    assertEquals(1, player.getTransactionArchive().getAllTransactions().size());

    // Marked as committed
    assertTrue(sale.isCommitted());
  }

  @Test
  void multipleSalesUpdateMoneyCorrectly() {
    // Add another share
    Share share2 = new Share(stock, new BigDecimal("5"), new BigDecimal("145.00"));
    player.getPortfolio().addShare(share2);

    stock.addNewSalesPrice(new BigDecimal("165.00"));
    SaleCalculator calculator2 = new SaleCalculator(share2);
    Sale sale2 = new Sale(share2, 1, calculator2);

    BigDecimal initialMoney = player.getCurrentMoney();

    sale.commit(player);
    sale2.commit(player);

    BigDecimal expected = initialMoney
        .add(sale.getTotalValue())
        .add(sale2.getTotalValue());

    assertEquals(expected, player.getCurrentMoney());
    assertEquals(2, player.getTransactionArchive().getAllTransactions().size());
  }
}
