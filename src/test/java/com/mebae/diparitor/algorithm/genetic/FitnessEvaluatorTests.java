package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FitnessEvaluatorTests {
  @Test
  public void evaluate_WithPowerDifficulty() {
    var powers = List.of(new Power("France", 2.0), new Power("Germany", 6.0));
    var players = List.of(new RegisteredPlayer("Anna", 3),
                          new RegisteredPlayer("Bill", 2),
                          new RegisteredPlayer("Charlie", 1));
    var games = List.of(new GeneticGame(0, List.of(players.getFirst(), players.get(1)), powers),
                        new GeneticGame(1, List.of(players.getFirst(), players.get(1)), powers),
                        new GeneticGame(2, List.of(players.getFirst(), players.get(2)), powers));
    var tournament = new GeneticTournament(games, powers.size(), true);

    assertEquals(5, FitnessScoreEvaluator.evaluate(tournament).powerDiversityScore());
    assertEquals(2, FitnessScoreEvaluator.evaluate(tournament).opponentDiversityScore());
    assertEquals(3.5556, FitnessScoreEvaluator.evaluate(tournament).powerBalanceScore(), 1e-4);
  }

  // TODO avec powerDifficulty (throw exception s'il n'y en a pas)

  @Test
  public void evaluate_WithoutPowerDifficulty() {
    var powers = List.of(new Power("France"), new Power("Germany"));
    var players = List.of(new RegisteredPlayer("Anna", 3),
                          new RegisteredPlayer("Bill", 2),
                          new RegisteredPlayer("Charlie", 1));
    var games = List.of(new GeneticGame(0, List.of(players.getFirst(), players.get(1)), powers),
                        new GeneticGame(1, List.of(players.getFirst(), players.get(1)), powers),
                        new GeneticGame(2, List.of(players.getFirst(), players.get(2)), powers));
    var tournament = new GeneticTournament(games, powers.size(), false);
    var expected = new FitnessScore(5, 2, 0);
    assertEquals(expected, FitnessScoreEvaluator.evaluate(tournament));
  }
}
