package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.data.Power;
import com.mebae.diparitor.data.RegisteredPlayer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mebae.diparitor.utils.DoubleUtils.computeAverage;
import static com.mebae.diparitor.utils.DoubleUtils.computeVariance;

final class FitnessEvaluator {
  private FitnessEvaluator() {
  }

  public static FitnessScore evaluate(PairingGenotype genotype, boolean hasDifficulty) {
    var playerPowerList = genotype.computePlayerPowerList();
    var powerDiversityScore = evaluatePowerDiversity(playerPowerList);
    var powerBalanceScore = evaluatePowerBalance(playerPowerList, hasDifficulty);
    var opponentDiversityScore = evaluateOpponentDiversity(genotype.computePlayerOpponentList());
    return new FitnessScore(powerDiversityScore, powerBalanceScore, opponentDiversityScore);
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
    return playerPowerList.values().stream().mapToDouble(FitnessEvaluator::computePlayerPowerBalance).boxed().toList();
  }

  private static double computePlayerPowerBalance(List<Power> powerList) {
    return powerList.stream().mapToDouble(Power::difficulty).average().orElseThrow();
  }

  private static long evaluateOpponentDiversity(Map<RegisteredPlayer, List<RegisteredPlayer>> playerOpponentList) {
    return evaluateDiversity(playerOpponentList);
  }

  private static <T> long evaluateDiversity(Map<RegisteredPlayer, List<T>> playerMap) {
    return playerMap.values().stream()
      .mapToLong(list -> list.stream()
        .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
        .values().stream()
        .filter(count -> count > 1)
        .mapToLong(count -> (count - 1) * (count - 1))
        .sum())
      .sum();
  }
}
