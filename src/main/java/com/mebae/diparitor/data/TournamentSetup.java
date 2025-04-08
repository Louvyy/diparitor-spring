package com.mebae.diparitor.data;

import java.util.Objects;
import java.util.Set;

/**
 * Represents the setup of a Diplomacy tournament.
 */
public final class TournamentSetup {
  private final Set<RegisteredPlayer> players;
  private final Set<Power> powers;
  private final int playerPerGameCount;
  private final int gameCount;

  private TournamentSetup(Set<RegisteredPlayer> players, Set<Power> powers, int playerPerGameCount, int gameCount) {
    this.players = players;
    this.powers = powers;
    this.playerPerGameCount = playerPerGameCount;
    this.gameCount = gameCount;
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
  public static TournamentSetup of(Set<RegisteredPlayer> players, Set<Power> powers) {
    Objects.requireNonNull(players);
    Objects.requireNonNull(powers);

    // TODO vérifier que players et powers ne contient pas null
    var playerPerGameCount = powers.size();
    var totalParticipationCount = players.stream().mapToInt(RegisteredPlayer::participationCount).sum();
    if (totalParticipationCount % playerPerGameCount != 0) {
      throw new IllegalArgumentException("The total game count is not divisible by the number of powers.");
    }
    var gameCount = totalParticipationCount / playerPerGameCount;

    return new TournamentSetup(players, powers, playerPerGameCount, gameCount);
  }

  /**
   * Returns the total number of games in the tournament.
   * This is determined by dividing the total number of game slots by the size of each game.
   *
   * @return the total number of games
   */
  public int getGameCount() {
    return gameCount;
  }

  /**
   * Returns the number of players per game, i.e., the size of each game, which is equal to the number of powers.
   *
   * @return the number of powers, representing the number of players per game
   */
  public int getPlayerPerGameCount() {
    return playerPerGameCount;
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
   * Returns the set of powers in the tournament.
   *
   * @return the set of powers
   */
  public Set<Power> getPowers() {
    return powers;
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
      "  Players per game: " + playerPerGameCount + ",\n" +
      "  Game count: " + gameCount + "\n" +
      '}';
  }
}
