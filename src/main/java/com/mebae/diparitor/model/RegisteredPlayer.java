package com.mebae.diparitor.model;

import java.util.Objects;
/**
 * Represents a registered player in a Diplomacy tournament.
 * A player has a name and the number of games they are registered for.
 */
public record RegisteredPlayer(String name, int participationCount) {
  /**
   * Constructs a registered player with a name and a specified game count.
   *
   * @param name               the name of the player
   * @param participationCount the number of games the player is registered for
   * @throws NullPointerException     if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code gameCount} is less than 1
   */
  public RegisteredPlayer {
    Objects.requireNonNull(name);
    if (participationCount < 1) {
      throw new IllegalArgumentException("A player must join at least one game");
    }
  }

  /**
   * Returns a string representation of the player, including their name and the number of games they are registered
   * for.
   *
   * @return the name and game count of the player
   */
  @Override
  public String toString() {
    return name;
  }
}
