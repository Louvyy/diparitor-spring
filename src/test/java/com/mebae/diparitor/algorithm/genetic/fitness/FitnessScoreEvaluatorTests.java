package com.mebae.diparitor.algorithm.genetic.fitness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mebae.diparitor.algorithm.genetic.model.GeneticGame;
import com.mebae.diparitor.algorithm.genetic.model.GeneticTournament;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class FitnessScoreEvaluatorTests {
  @Test
  public void evaluate_WithPowerDifficulty() {
    var powers = List.of(Power.withDifficulty("France", 2.0), Power.withDifficulty("Germany", 6.0));
    var players = List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Bill", 2),
                          new RegisteredPlayer("Charlie", 1));
    var games = List.of(new GeneticGame(List.of(players.getFirst(), players.get(1)), powers),
                        new GeneticGame(List.of(players.getFirst(), players.get(1)), powers),
                        new GeneticGame(List.of(players.getFirst(), players.get(2)), powers));
    var tournament = new GeneticTournament(games, powers.size(), true);

    assertEquals(5, FitnessScoreEvaluator.evaluate(tournament).powerDiversityScore());
    assertEquals(2, FitnessScoreEvaluator.evaluate(tournament).opponentDiversityScore());
    assertEquals(3.5556, FitnessScoreEvaluator.evaluate(tournament).powerBalanceScore(), 1e-4);
  }

  // TODO TESTS avec powerDifficulty (throw exception s'il n'y en a pas)

  @Test
  public void evaluate_WithoutPowerDifficulty() {
    var powers = List.of(Power.withoutDifficulty("France"), Power.withoutDifficulty("Germany"));
    var players = List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Bill", 2),
                          new RegisteredPlayer("Charlie", 1));
    var games = List.of(new GeneticGame(List.of(players.getFirst(), players.get(1)), powers),
                        new GeneticGame(List.of(players.getFirst(), players.get(1)), powers),
                        new GeneticGame(List.of(players.getFirst(), players.get(2)), powers));
    var tournament = new GeneticTournament(games, powers.size(), false);
    var expected = new FitnessScore(5, 2, 0);
    assertEquals(expected, FitnessScoreEvaluator.evaluate(tournament));
  }

  @Test
  void duplicateCounts() {
    var set = Stream.of("A", "A", "B", "A", "C", "A", "C")
        .gather(FitnessScoreEvaluator.duplicateCounts())
        .collect(Collectors.toSet());
    assertEquals(2, set.size());
    assertTrue(set.contains(2L));
    assertTrue(set.contains(4L));
  }
}
