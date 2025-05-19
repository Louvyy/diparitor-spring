package com.mebae.diparitor.algorithm.genetic.initializer;

import com.mebae.diparitor.algorithm.genetic.model.GeneticTournament;
import com.mebae.diparitor.model.TournamentSetup;

/**
 * Functional interface representing a strategy for generating an initial genetic tournament.
 *
 * <p>
 * Implementations of this interface define how to create an initial {@link GeneticTournament}
 * from a given {@link TournamentSetup}.
 *
 * @see GeneticTournament
 * @see TournamentSetup
 */
@FunctionalInterface
public interface InitialTournamentGenerator {
  /**
   * Generates an initial tournament based on a specific strategy.
   *
   * @param tournamentSetup the configuration of the tournament, including the list of players,
   *                        number of games, powers, and participation constraints.
   * @return an initial {@link GeneticTournament} ready for evaluation or optimization.
   */
  GeneticTournament generate(TournamentSetup tournamentSetup);
}
