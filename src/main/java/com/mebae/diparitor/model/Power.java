package com.mebae.diparitor.model;

import java.util.Objects;
import java.util.Optional;

// TODO JAVADOC
/**
 * Represents a power in a Diplomacy game.
 * A power can have a difficulty coefficient or none at all.
 */
public final class Power {
  private final String name;
  /**
   * Represents the value of the difficulty coefficient if any, null otherwise.
   */
  private final Double difficulty;

  /**
   * Constructs a power with a difficulty coefficient.
   *
   * @param name       the name of the power
   * @param difficulty the difficulty coefficient
   * @throws NullPointerException     if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code difficulty} is negative
   */
  private Power(String name, Double difficulty) {
    this.name = Objects.requireNonNull(name);
    this.difficulty = difficulty;
  }

  public static Power withDifficulty(String name, double difficulty) {
    if (difficulty < 0.0) {
      throw new IllegalArgumentException(
          "The difficulty coefficient must be a non-negative number");
    }
    return new Power(name, difficulty);
  }

  public static Power withoutDifficulty(String name) {
    return new Power(name, null);
  }

  /**
   * Returns the name of the power.
   *
   * @return the name of the power
   */
  public String name() {
    return name;
  }

  /**
   * Returns the difficulty coefficient, if any.
   *
   * @return the difficulty value, or {@code Optional.empty()} if not set
   */
  public Optional<Double> difficulty() {
    return Optional.ofNullable(difficulty);
  }

  /**
   * Indicates whether a difficulty has been set for this power.
   *
   * @return {@code true} if a difficulty is defined, {@code false} otherwise
   */
  public boolean hasDifficulty() {
    return difficulty != null;
  }

  /**
   * Returns a string representation of the power.
   * If a difficulty is set, it is included in the output.
   *
   * @return the name of the power, optionally followed by its difficulty
   */
  @Override
  public String toString() {
    return hasDifficulty() ? name + " (difficulty: " + difficulty + ")" : name;
  }

  /**
   * Checks if this power is equal to another power.
   * Two powers are considered equal if they have the same name and the same difficulty (if
   * defined).
   *
   * @param o the other object to compare
   * @return {@code true} if the two powers are equal, {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Power power = (Power) o;
    return name.equals(power.name) && Objects.equals(difficulty, power.difficulty);
  }

  /**
   * Computes a hash code for this power.
   * The hash code is based on the name and difficulty of the power.
   *
   * @return the hash code of this power
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, difficulty);
  }
}
