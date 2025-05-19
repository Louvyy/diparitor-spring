package com.mebae.diparitor.algorithm.genetic.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GeneticTournamentTests {
  @Test
  public void isViable_ReturnsTrue() {
    var powers = List.of(Power.withDifficulty("France", 2.0), Power.withDifficulty("Germany", 6.0));
    var game1 =
        new GeneticGame(List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Bill", 2)),
                        powers);
    var game2 =
        new GeneticGame(List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Bill", 2)),
                        powers);
    var game3 = new GeneticGame(
        List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Charlie", 1)), powers);
    var gameList = List.of(game1, game2, game3);
    var tournament = new GeneticTournament(gameList, powers.size(), true);
    assertTrue(tournament.isViable());
  }

  @Test
  public void isViable_ReturnsFalse() {
    var powers = List.of(Power.withDifficulty("France", 2.0), Power.withDifficulty("Germany", 6.0));
    var game1 =
        new GeneticGame(List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Bill", 2)),
                        powers);
    var game2 = new GeneticGame(
        List.of(new RegisteredPlayer("Charlie", 1), new RegisteredPlayer("Bill", 2)), powers);
    var game3 =
        new GeneticGame(List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Anna", 3)),
                        powers);
    var gameList = List.of(game1, game2, game3);
    var tournament = new GeneticTournament(gameList, powers.size(), true);
    assertFalse(tournament.isViable());
  }
}
