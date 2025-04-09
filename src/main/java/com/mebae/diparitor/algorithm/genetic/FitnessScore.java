package com.mebae.diparitor.algorithm.genetic;

record FitnessScore(long powerDiversityScore, long opponentDiversityScore, double powerBalanceScore) {
  // TODO pondérer (70% d'importance pour opponentDiversityScore - et 30% sur powerBalanceScore)
  public boolean isBetterThan(FitnessScore other) {
    if (powerDiversityScore < other.powerDiversityScore) {
      return true;
    }
    if (powerDiversityScore > other.powerDiversityScore) {
      return false;
    }

    if (opponentDiversityScore < other.opponentDiversityScore) {
      return true;
    }
    if (opponentDiversityScore > other.opponentDiversityScore) {
      return false;
    }

    return powerBalanceScore < other.powerBalanceScore;
  }
}
