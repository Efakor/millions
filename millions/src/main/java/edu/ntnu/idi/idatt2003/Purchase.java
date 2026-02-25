package edu.ntnu.idi.idatt2003;

import java.math.BigDecimal;

public class Purchase extends Transaction {
  public Purchase(Share share,int week) {
    super(share,week,new PurchaseCalculator(share));

  }
  @Override
  public  void commit(Player player){
    if (isCommitted()){
      throw new IllegalStateException("Transaction is already committed");
    }
    BigDecimal totalCost=getCalculator().calculateTotal();
    player.withdrawMoney(totalCost);
    player.getPortfolio().addShare(getShare());
    player.getTransactionArchive().add(this);
    setCommitted(true);
  }

}
