package com.mebae.diparitor.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the setup of a Diplomacy tournament.
 */
public final class TournamentSetup {
  private final Set<RegisteredPlayer> players;
  private final List<Power> powers;
  private final boolean hasPowerDifficulty;

  private TournamentSetup(Set<RegisteredPlayer> players, List<Power> powers, int playerPerGameCount, int gameCount,
                          boolean hasPowerDifficulty) {
    if (playerPerGameCount < 2) {
      throw new IllegalArgumentException("There must be at least 2 players in a single game");
    }
    if (gameCount < 1) {
      throw new IllegalArgumentException("There must be at least 1 game");
    }
    this.powers = List.copyOf(powers);
    if (hasPowerDifficulty && this.powers.stream().map(Power::difficulty).anyMatch(Optional::isEmpty)) {
      throw new IllegalArgumentException("A difficulty must be specified in all powers");
    }
    this.players = Set.copyOf(players);
    this.hasPowerDifficulty = hasPowerDifficulty;
  }

  /**
   * Factory method to create a TournamentSetup instance with validation.
   * This method checks that the total participation count is divisible by the number of player per game.
   *
   * @param players the set of registered players
   * @param powers  the set of powers in the tournament
   * @return a valid TournamentSetup instance
   * @throws IllegalArgumentException if the total game count is not divisible by the number of powers
   */
  public static TournamentSetup of(Set<RegisteredPlayer> players, Set<Power> powers, boolean hasPowerDifficulty) {
    var playersCopy = Set.copyOf(players);
    var powersCopy = List.copyOf(powers);

    var playerPerGameCount = powersCopy.size();
    var totalParticipationCount = playersCopy.stream()
      .mapToInt(RegisteredPlayer::participationCount)
      .sum();
    if (totalParticipationCount % playerPerGameCount != 0) {
      throw new IllegalArgumentException("The total game count is not divisible by the number of powers.");
    }
    var gameCount = totalParticipationCount / playerPerGameCount;
    return new TournamentSetup(playersCopy, powersCopy, playerPerGameCount, gameCount, hasPowerDifficulty);
  }

  public boolean hasPowerDifficulty() {
    return hasPowerDifficulty;
  }

  /**
   * Returns the set of players in the tournament.
   *
   * @return the set of registered players
   */
  public Set<RegisteredPlayer> getPlayers() {
    return players;
  }

  /**
   * Returns the list of powers in the tournament.
   *
   * @return the list of powers
   */
  public List<Power> getPowers() {
    return List.copyOf(powers);
  }

  /**
   * Returns a string representation of the tournament setup.
   * This includes the number of players, powers, players per game, and the total number of games.
   *
   * @return a formatted string summarizing the tournament setup
   */
  @Override
  public String toString() {
    return "TournamentSetup {\n" +
      "  Players (" + players.size() + "): " + players + ",\n" +
      "  Powers (" + powers.size() + "): " + powers + ",\n" +
      '}';
  }
}
