package edu.ntnu.idi.idatt2003.observer;

/**
 * The interface to be implemented by any class that wants to
 * receive notifications from an Observable (e.g. Exchange).
 */
public interface GameObserver {

  /**
   * Called by the Observable when a game event occurs.
   *
   * @param event the event that was triggered
   */
  void onGameEvent(GameEvent event);
}
