package edu.ntnu.idi.idatt2003.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the purchase class.
 * Tests the creation and commitment of purchase transactions.
 */

class PurchaseTest {
  private Stock appleStock;
  private Share appleShare;
  private Player player;
  private Purchase purchase;

  /**
   * Sets up test fixtures before each test.
   * Creates a stock, share, player, and purchase transaction for testing.
   */

  @BeforeEach
  void setUp() {
    appleStock = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
    appleShare = new Share(appleStock, new BigDecimal("10"), new BigDecimal("100.00"));
    player = new Player("Test Player", new BigDecimal("10000.00"));
    purchase = new Purchase(appleShare, 1);
  }

  /**
   * Tests that a Purchase can be created with valid inputs.
   */
  @Nested
  class ConstructionTests {
    /**
     * Tests that commit deducts the correct amount form player's money.
     */
    @Test
    void testConstructionWithValidInputs() {
      assertNotNull(purchase);
      assertEquals(appleShare, purchase.getShare());
      assertEquals(1, purchase.getWeek());
      assertFalse(purchase.isCommitted());
    }

  }

  @Nested
  class SuccessfulCommitTests {
    /**
     * Test that commit deducts the correct amount from player's money
     */
    @Test
    void testCommitsDeductsMoney() {
      BigDecimal initialMoney = player.getCurrentMoney();
      BigDecimal totalcost = purchase.getTotalValue();
      purchase.commit(player);
      BigDecimal expectedMoney = initialMoney.subtract(totalcost);
      assertEquals(0, expectedMoney.compareTo(player.getCurrentMoney()));

    }

    /**
     * Tests that commit adds the share to the player's portfolio.
     */
    @Test
    void testCommitAddShareToPortfolio() {
      purchase.commit(player);
      assertTrue(player.getPortfolio().contains(appleShare));
      assertEquals(1, player.getPortfolio().size());
    }

    /**
     * Tests that commit records the transaction in the player's archive.
     */
    @Test
    void testCommitRecordsTransaction() {
      purchase.commit(player);

      assertFalse(player.getTransactionArchive().isEmpty());
      assertTrue(player.getTransactionArchive().getAllTransactions().contains(purchase));
    }

    /**
     * Tests that commit sets the committed flag to true.
     */
    @Test
    void testCommitSetsCommittedFlag() {
      assertFalse(purchase.isCommitted());
      purchase.commit(player);
      assertTrue(purchase.isCommitted());
    }

    /**
     * Tests that multiple purchases can be commited for the same player.
     */
    @Test
    void testMultiplePurchases() {
      Stock googleStock = new Stock("GOOGL", "Alphabet", new BigDecimal("2800.00"));
      Share googleShare = new Share(googleStock, new BigDecimal("2"), new BigDecimal("2750.00"));
      Purchase purchase2 = new Purchase(googleShare, 1);
      purchase.commit(player);
      purchase2.commit(player);
      assertEquals(2, player.getPortfolio().size());
      assertEquals(2, player.getTransactionArchive().getAllTransactions().size());


    }


  }
  @Nested
  class ValidationMethods{
    /**
     * Tests that commit throws IllegalArgumentException when player is null.
     */
    @Test
    void testCommitWithNullPlayer(){
      assertThrows(IllegalArgumentException.class, () -> purchase.commit(null));

    }

    /**
     * Tests that commit  throws an exception when player has insufficient funds.
     */
    @Test
    void testCommitWithInnsufficientfunds(){
      Player poorPlayer = new Player("Poor Player", new BigDecimal("10.00"));
      assertThrows(IllegalStateException.class, () -> purchase.commit(poorPlayer));
    }

    /**
     * Test that commit throws an exception when called on already commited
     * transaction.
     */
    @Test
    void testCommitWhenAlreadyCommitted(){
      purchase.commit(player);
      assertThrows(IllegalStateException.class, () -> purchase.commit(player));

    }

    /**
     * Test that null player validation before commited check.
     */
    @Test
    void testValidationNullPlayerFirst(){
      assertThrows(IllegalArgumentException.class, () -> purchase.commit(null));
    }
    /**
     * Tests that remains uncommited after failed commit due to null player.
     */
    @Test
    void testPlayerUncommittedAfterNullPlayerError(){
      try{
        purchase.commit(null);
        fail("Should have thrown IllegalArgumentException");
      } catch(IllegalArgumentException e){


      }
      assertFalse(purchase.isCommitted());
    }

    /**
     * Test that transaction remains uncommited after insufficient funds
     */
    @Test
    void testPlayerUncommitedAfterInsufficientFunds(){
      Player poorPlayer = new Player("Poor Player", new BigDecimal("10.00"));
      try{
        purchase.commit(null);
        fail("Should have thrown IllegalArgumentException");
    }catch(IllegalArgumentException e){

      }
      assertFalse(purchase.isCommitted());
      assertEquals(0,poorPlayer.getPortfolio().size());

    }
  }



  }

