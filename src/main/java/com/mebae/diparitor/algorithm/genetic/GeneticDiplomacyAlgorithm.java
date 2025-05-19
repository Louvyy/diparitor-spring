package com.mebae.diparitor.algorithm.genetic;

import static com.mebae.diparitor.utils.RandomUtils.getSeed;

import com.mebae.diparitor.algorithm.DiplomacyAlgorithm;
import com.mebae.diparitor.algorithm.genetic.fitness.FitnessScoreEvaluator;
import com.mebae.diparitor.algorithm.genetic.initializer.InitialTournamentGenerator;
import com.mebae.diparitor.algorithm.genetic.model.GeneticTournament;
import com.mebae.diparitor.model.PairingResult;
import com.mebae.diparitor.model.TournamentSetup;
import java.util.Objects;
import java.util.logging.Logger;

// TODO JAVADOC
public final class GeneticDiplomacyAlgorithm implements DiplomacyAlgorithm {
  private static final Logger LOGGER = Logger.getLogger(GeneticDiplomacyAlgorithm.class.getName());
  private static final int MAX_FAILED_MUTATIONS = 100_000;

  private static GeneticTournament computeRandomViableTournament(TournamentSetup tournamentSetup,
                                                                 InitialTournamentGenerator initialTournamentGenerator) {
    return initialTournamentGenerator.generate(tournamentSetup);
  }

  @Override
  public PairingResult computeBestTournament(TournamentSetup tournamentSetup,
                                             InitialTournamentGenerator initialTournamentGenerator) {
    Objects.requireNonNull(tournamentSetup);

    var bestTournament = computeRandomViableTournament(tournamentSetup, initialTournamentGenerator);
    var bestFitnessScore = FitnessScoreEvaluator.evaluate(bestTournament);
    var failedMutationStreak = 0;

    while (failedMutationStreak < MAX_FAILED_MUTATIONS) {
      var mutatedTournament = bestTournament.computeRandomViablePlayerSwap();
      var mutatedFitnessScore = FitnessScoreEvaluator.evaluate(mutatedTournament);
      if (mutatedFitnessScore.compareTo(bestFitnessScore) < 0) {
        bestTournament = mutatedTournament;
        bestFitnessScore = mutatedFitnessScore;
        failedMutationStreak = 0;
        LOGGER.info("Fitness score improvement : " + bestFitnessScore);
      } else {
        failedMutationStreak++;
      }
    }

    LOGGER.info("Optimization complete. Final fitness score : " + bestFitnessScore);
    LOGGER.info("Seed used : " + getSeed());
    return bestTournament.toPairingResult();
  }
}
