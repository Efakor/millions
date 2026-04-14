package edu.ntnu.idi.idatt2003.observer;

/**
 * Implemented by classes that can be observed.
 * Allows observers to register and receive event notifications.
 */
public interface Observable {

  /**
   * Registers an observer to receive event notifications.
   *
   * @param observer the observer to add
   */
  void addObserver(GameObserver observer);

  /**
   * Removes a previously registered observer.
   *
   * @param observer the observer to remove
   */
  void removeObserver(GameObserver observer);

  /**
   * Notifies all registered observers of an event.
   *
   * @param event the event to broadcast
   */
  void notifyObservers(GameEvent event);
}
