package edu.ntnu.idi.idatt2003.observer;

import edu.ntnu.idi.idatt2003.model.Stock;
import edu.ntnu.idi.idatt2003.model.Transaction;

/**
 * Observer Interfaces for Millions game.
 * Classes implementing the interface will be notified of game events.
 */
public interface GameObserver {
  void weekAdvanced(int newWeek);
  void transactionCompleted(Transaction transaction);
  void stockPriceChanged(Stock stock);
}
