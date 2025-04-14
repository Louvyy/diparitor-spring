package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.algorithm.Algorithm;
import com.mebae.diparitor.model.PairingResult;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import com.mebae.diparitor.model.TournamentSetup;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class GeneticAlgorithm implements Algorithm {
  private static final Logger logger = Logger.getLogger(GeneticAlgorithm.class.getName());

  private static GeneticTournament computeRandomViableTournament(TournamentSetup tournamentSetup) {
    var players = tournamentSetup.getPlayers();
    var powers = tournamentSetup.getPowers();
    var gameList = generateRandomGameList(players, powers);
    var tournament = new GeneticTournament(gameList, powers.size(), tournamentSetup.hasPowerDifficulty());
    while (!tournament.isViable()) {
      tournament.computeRandomViablePlayerSwap();
    }
    return tournament;
  }

  private static ArrayList<GeneticGame> generateRandomGameList(Set<RegisteredPlayer> players, List<Power> powers) {
    var participantList = generateShuffledParticipantList(players);
    var geneCount = powers.size();
    return IntStream.range(0, participantList.size() / geneCount)
      .mapToObj(i -> new GeneticGame(i, participantList.subList(i * geneCount, (i + 1) * geneCount), powers))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  private static ArrayList<RegisteredPlayer> generateShuffledParticipantList(Set<RegisteredPlayer> players) {
    var participantList = players.stream()
      .flatMap(player -> Stream.generate(() -> player).limit(player.participationCount()))
      .collect(Collectors.toCollection(ArrayList::new));
    Collections.shuffle(participantList);
    return participantList;
  }

  @Override
  public PairingResult computeBestTournament(TournamentSetup tournamentSetup) {
    Objects.requireNonNull(tournamentSetup);

    var bestTournament = computeRandomViableTournament(tournamentSetup);
    var bestFitnessScore = FitnessScoreEvaluator.evaluate(bestTournament);
    var failedMutationStreak = 0;
    // TODO : ajuster dynamiquement le nombre max de mutations non améliorantes selon la taille du tournoi
    final var maxFailedMutations = 100_000;

    while (failedMutationStreak < maxFailedMutations) {
      var mutatedTournament = bestTournament.mutableCopyOf();
      mutatedTournament.computeRandomViablePlayerSwap();
      var mutatedFitnessScore = FitnessScoreEvaluator.evaluate(mutatedTournament);
      if (mutatedFitnessScore.compareTo(bestFitnessScore) < 0) {
        bestTournament = mutatedTournament.mutableCopyOf();
        bestFitnessScore = mutatedFitnessScore;
        failedMutationStreak = 0;
        logger.info("Fitness score improvement : " + bestFitnessScore);
      } else {
        failedMutationStreak++;
      }
    }

    logger.info("Optimization complete. Final fitness score : " + bestFitnessScore);
    return bestTournament.toPairingResult();
  }
}
