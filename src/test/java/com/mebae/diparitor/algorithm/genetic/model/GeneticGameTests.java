package com.mebae.diparitor.algorithm.genetic.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GeneticGameTests {
  @Test
  public void isViable_ReturnsTrue() {
    var players = List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Bill", 2),
                          new RegisteredPlayer("Charlie", 1));
    var powers = List.of(Power.withDifficulty("France", 2.0), Power.withDifficulty("Germany", 6.0));
    var game = new GeneticGame(players, powers);
    assertTrue(game.isViable());
  }

  @Test
  public void isViable_ReturnsFalse() {
    var players = List.of(new RegisteredPlayer("Anna", 3), new RegisteredPlayer("Bill", 2),
                          new RegisteredPlayer("Anna", 3));
    var powers = List.of(Power.withDifficulty("France", 2.0), Power.withDifficulty("Germany", 6.0));
    var game = new GeneticGame(players, powers);
    assertFalse(game.isViable());
  }
}
