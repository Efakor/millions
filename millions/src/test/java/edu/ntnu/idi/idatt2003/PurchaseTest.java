package edu.ntnu.idi.idatt2003;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the purchase class.
 */

class PurchaseTest {
  private Stock appleStock;
  private Share appleShare;
  private Player player;
  private Purchase purchase;

  @BeforeEach
  void setUp() {
    appleStock=new Stock("AAPL","Apple Inc.",new BigDecimal("150.00"));
    appleShare=new Share(appleStock,new BigDecimal("10"),new BigDecimal("100.00"));
    player=new Player("Test Player",new BigDecimal("10000.00"));
    purchase=new Purchase(appleShare,1);
  }
  @Test
  void testConstructionWithValidInputs(){
    assertNotNull(purchase);
    assertEquals(appleShare,purchase.getShare());
    assertEquals(1,purchase.getWeek());
    assertFalse(purchase.isCommitted());
  }
  @Test
  void testCommitsDeductsMoney(){
    BigDecimal initialMoney=player.getCurrentMoney();
    BigDecimal totalcost=purchase.getTotalValue();
    purchase.commit(player);
    BigDecimal expectedMoney=initialMoney.subtract(totalcost);
    assertEquals(0,expectedMoney.compareTo(player.getCurrentMoney()));

  }
  @Test
  void testCommitAddShareToPortfolio(){
    purchase.commit(player);
    assertTrue(player.getPortfolio().contains(appleShare));
    assertEquals(1,player.getPortfolio().size());
  }
  @Test
  void testCommitSetsCommittedFlag(){
    assertFalse(purchase.isCommitted());
    purchase.commit(player);
    assertTrue(purchase.isCommitted());
  }
  @Test
  void testSetsCommitWithInsufficientFunds() {
    Player poorPlayer=new Player("Poor player",new BigDecimal("10.00"));
    assertThrows(IllegalStateException.class,()->purchase.commit(poorPlayer));

  }
  @Test
  void testMultiplePurchases(){
    Stock googleStock =new Stock("GOOGL","Alphabet", new BigDecimal("2800.00"));
    Share googleShare=new Share(googleStock,new BigDecimal("2"),new BigDecimal("2750"));
    Purchase purchase2=new Purchase(googleShare,1);
    purchase.commit(player);
    purchase2.commit(player);
    assertEquals(2,player.getPortfolio().size());
    assertEquals(2,player.getTransactionArchive().getAllTransactions().size());

  }
}
