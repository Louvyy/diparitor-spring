package com.mebae.diparitor.algorithm.genetic.fitness;

import static com.mebae.diparitor.utils.DoubleUtils.computeVariance;

import com.mebae.diparitor.algorithm.genetic.model.GeneticTournament;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Gatherer;

// TODO JAVADOC
public final class FitnessScoreEvaluator {
  private FitnessScoreEvaluator() {
  }

  public static FitnessScore evaluate(GeneticTournament tournament) {
    var playerPowerList = tournament.computePlayerPowerList();
    var powerDiversityScore = evaluatePowerDiversity(playerPowerList);
    var powerBalanceScore = evaluatePowerBalance(playerPowerList, tournament.hasPowerDifficulty());
    var opponentDiversityScore = evaluateOpponentDiversity(tournament.computePlayerOpponentList());
    return new FitnessScore(powerDiversityScore, opponentDiversityScore, powerBalanceScore);
  }

  private static long evaluatePowerDiversity(Map<RegisteredPlayer, List<Power>> playerPowerList) {
    return evaluateDiversity(playerPowerList);
  }

  // Variance
  private static double evaluatePowerBalance(Map<RegisteredPlayer, List<Power>> playerPowerList,
                                             boolean hasDifficulty) {
    if (!hasDifficulty) {
      return 0.0;
    }
    var powerBalanceByPlayer = computePowerBalanceByPlayer(playerPowerList);
    return computeVariance(powerBalanceByPlayer);
  }

  private static List<Double> computePowerBalanceByPlayer(
      Map<RegisteredPlayer, List<Power>> playerPowerList) {
    return playerPowerList.values()
        .stream()
        .map(FitnessScoreEvaluator::computeAveragePowerDifficulty)
        .toList();
  }

  private static double computeAveragePowerDifficulty(List<Power> powerList) {
    return powerList.stream()
        .mapToDouble(power -> power.difficulty().orElseThrow())
        .average()
        .orElseThrow();
  }

  private static long evaluateOpponentDiversity(
      Map<RegisteredPlayer, List<RegisteredPlayer>> playerOpponentList) {
    return evaluateDiversity(playerOpponentList);
  }

  static <T> Gatherer<T, Map<T, Long>, Long> duplicateCounts() {
    return Gatherer.of(HashMap::new, Gatherer.Integrator.ofGreedy((state, element, _) -> {
      state.merge(element, 1L, Long::sum);
      return true;
    }), (map1, map2) -> {
      map1.forEach((key, value) -> map2.merge(key, value, Long::sum));
      return map2;
    }, (state, downstream) -> state.forEach((_, value) -> {
      if (value > 1) {
        downstream.push(value);
      }
    }));
  }

  // TODO rendre + lisible
  private static <T> long evaluateDiversity(Map<RegisteredPlayer, List<T>> playerMap) {
    return playerMap.values()
        .stream()
        .mapToLong(list -> list.stream()
            .gather(duplicateCounts())
            .mapToLong(count -> (count - 1) * (count - 1))
            .sum())
        .sum();
  }
}
