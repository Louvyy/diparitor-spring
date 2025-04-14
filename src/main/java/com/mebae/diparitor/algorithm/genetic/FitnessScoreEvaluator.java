package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mebae.diparitor.utils.DoubleUtils.computeVariance;

final class FitnessScoreEvaluator {
  private FitnessScoreEvaluator() {
  }

  public static FitnessScore evaluate(GeneticTournament genotype) {
    var playerPowerList = genotype.computePlayerPowerList();
    var powerDiversityScore = evaluatePowerDiversity(playerPowerList);
    var powerBalanceScore = evaluatePowerBalance(playerPowerList, genotype.hasPowerDifficulty());
    var opponentDiversityScore = evaluateOpponentDiversity(genotype.computePlayerOpponentList());
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

  private static List<Double> computePowerBalanceByPlayer(Map<RegisteredPlayer, List<Power>> playerPowerList) {
    return playerPowerList.values().stream().map(FitnessScoreEvaluator::computeAveragePowerDifficulty).toList();
  }

  private static double computeAveragePowerDifficulty(List<Power> powerList) {
    return powerList.stream().mapToDouble(power -> power.difficulty().orElseThrow()).average().orElseThrow();
  }

  private static long evaluateOpponentDiversity(Map<RegisteredPlayer, List<RegisteredPlayer>> playerOpponentList) {
    return evaluateDiversity(playerOpponentList);
  }

  private static <T> long evaluateDiversity(Map<RegisteredPlayer, List<T>> playerMap) {
    return playerMap.values()
      .stream()
      .mapToLong(list -> list.stream()
        // TODO gatherers en java 24
        .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
        .values()
        .stream()
        .filter(count -> count > 1)
        .mapToLong(count -> (count - 1) * (count - 1))
        .sum())
      .sum();
  }
}
