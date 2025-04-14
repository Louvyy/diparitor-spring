package com.mebae.diparitor.algorithm.genetic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FitnessScoreTests {
  @Test
  public void compareTo_SmallerPowerDiversityScore() {
    var fitnessScore = new FitnessScore(5, 5, 4.2);
    var comparedFitnessScore = new FitnessScore(4, 10, 6.4);
    assertEquals(1, fitnessScore.compareTo(comparedFitnessScore));
  }

  @Test
  public void compareTo_BiggerPowerDiversityScore() {
    var fitnessScore = new FitnessScore(5, 5, 4.2);
    var comparedFitnessScore = new FitnessScore(6, 3, 2.1);
    assertEquals(-1, fitnessScore.compareTo(comparedFitnessScore));
  }

  @Test
  public void compareTo_SmallerOpponentDiversityScore() {
    var fitnessScore = new FitnessScore(5, 5, 4.2);
    var comparedFitnessScore = new FitnessScore(5, 3, 4.9);
    assertEquals(1, fitnessScore.compareTo(comparedFitnessScore));
  }

  @Test
  public void compareTo_BiggerOpponentDiversityScore() {
    var fitnessScore = new FitnessScore(5, 5, 4.2);
    var comparedFitnessScore = new FitnessScore(5, 8, 3.5);
    assertEquals(-1, fitnessScore.compareTo(comparedFitnessScore));
  }

  @Test
  public void compareTo_SmallerPowerBalanceScore() {
    var fitnessScore = new FitnessScore(5, 5, 4.2);
    var comparedFitnessScore = new FitnessScore(5, 5, 3.5);
    assertEquals(1, fitnessScore.compareTo(comparedFitnessScore));
  }

  @Test
  public void compareTo_BiggerPowerBalanceScore() {
    var fitnessScore = new FitnessScore(5, 5, 4.2);
    var comparedFitnessScore = new FitnessScore(5, 5, 8.5);
    assertEquals(-1, fitnessScore.compareTo(comparedFitnessScore));
  }

  @Test
  public void compareTo_SameFitnessScore() {
    var fitnessScore = new FitnessScore(5, 5, 4.2);
    var comparedFitnessScore = new FitnessScore(5, 5, 4.2);
    assertEquals(0, fitnessScore.compareTo(comparedFitnessScore));
  }
}
