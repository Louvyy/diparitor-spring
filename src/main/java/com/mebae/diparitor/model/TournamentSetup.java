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
  private final int gameCount;

  private TournamentSetup(Set<RegisteredPlayer> players, List<Power> powers,
                          boolean hasPowerDifficulty, int gameCount) {
    this.players = Set.copyOf(players);
    this.powers = List.copyOf(powers);
    this.hasPowerDifficulty = hasPowerDifficulty;
    if (gameCount < 1) {
      throw new IllegalArgumentException("There must be at least 1 game");
    }
    this.gameCount = gameCount;
  }

  /**
   * Factory method to create a TournamentSetup instance with validation.
   * This method checks that the total participation count is divisible by the number of player
   * per game.
   *
   * @param players the set of registered players
   * @param powers  the set of powers in the tournament
   * @return a valid TournamentSetup instance
   * @throws IllegalArgumentException if the total game count is not divisible by the number of
   *                                  powers
   */
  public static TournamentSetup of(Set<RegisteredPlayer> players, Set<Power> powers,
                                   boolean hasPowerDifficulty) {
    var playersCopy = Set.copyOf(players);
    var powersCopy = List.copyOf(powers);
    if (hasPowerDifficulty && powersCopy.stream()
        .map(Power::difficulty)
        .anyMatch(Optional::isEmpty)) {
      throw new IllegalArgumentException("A difficulty must be specified in all powers");
    }
    var powerCount = powersCopy.size();
    var totalParticipationCount =
        playersCopy.stream().mapToInt(RegisteredPlayer::participationCount).sum();
    if (totalParticipationCount % powerCount != 0) {
      throw new IllegalArgumentException(
          "The total participation count must be divisible by the number of powers");
    }
    if (powerCount < 2) {
      throw new IllegalArgumentException("There must be at least 2 powers");
    }
    var gameCount = totalParticipationCount / powerCount;
    var maxParticipationCount =
        playersCopy.stream().mapToInt(RegisteredPlayer::participationCount).max().orElseThrow();
    if (maxParticipationCount > gameCount) {
      throw new IllegalArgumentException(
          "A player can't participate in more than the total of games");
    }
    return new TournamentSetup(playersCopy, powersCopy, hasPowerDifficulty, gameCount);
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

  public int getGameCount() {
    return gameCount;
  }

  /**
   * Returns a string representation of the tournament setup.
   * This includes the number of players, powers, players per game, and the total number of games.
   *
   * @return a formatted string summarizing the tournament setup
   */
  @Override
  public String toString() {
    return "TournamentSetup {\n" + "  Players (" + players.size() + "): " + players + ",\n"
        + "  Powers (" + powers.size() + "): " + powers + ",\n" + '}';
  }
}
