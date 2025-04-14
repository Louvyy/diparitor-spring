package com.mebae.diparitor.algorithm.genetic;

record FitnessScore(long powerDiversityScore, long opponentDiversityScore, double powerBalanceScore)
  implements Comparable<FitnessScore> {
  @Override
  public int compareTo(FitnessScore other) {
    if (powerDiversityScore < other.powerDiversityScore) return -1;
    if (powerDiversityScore > other.powerDiversityScore) return 1;
    if (opponentDiversityScore < other.opponentDiversityScore) return -1;
    if (opponentDiversityScore > other.opponentDiversityScore) return 1;

    return Double.compare(powerBalanceScore, other.powerBalanceScore);
  }
}
