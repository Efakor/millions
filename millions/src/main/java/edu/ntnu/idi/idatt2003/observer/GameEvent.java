package edu.ntnu.idi.idatt2003.observer;

/**
 * Represents the kinds of events that may occur in the game.
 * The Exchange class fires these events to prompt all registered GameObservers.
 */
public enum GameEvent {
  SALE_MADE,
  PURCHASE_MADE,
  WEEK_ADVANCED,
  GAME_STARTED,
  GAME_ENDED
}
