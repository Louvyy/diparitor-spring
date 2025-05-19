package com.mebae.diparitor.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.IntStream;

// TODO JAVADOC
public final class RandomUtils {
  private static final String GENERATOR_NAME = "L64X128MixRandom";
  private static final long SEED = System.nanoTime();
  private static final RandomGenerator GENERATOR =
      RandomGeneratorFactory.of(GENERATOR_NAME).create(SEED);

  public static long getSeed() {
    return SEED;
  }

  public static int randomNonNegativeNumber(int upperBound) {
    if (upperBound < 1) {
      throw new IllegalArgumentException("Upper bound must be at least 0 to get a number");
    }
    return GENERATOR.nextInt(upperBound);
  }

  public static int randomNonNegativeNumberExcept(int upperBound, int exception) {
    if (upperBound < 2) {
      throw new IllegalArgumentException("Upper bound must be at least 1 to get a number");
    }
    return pickRandom(IntStream.range(0, upperBound).filter(x -> x != exception).boxed().toList());
  }

  public static <T> T pickRandom(Collection<T> collection) {
    if (collection.isEmpty()) {
      throw new IllegalArgumentException("Cannot pick from an empty collection");
    }

    var randomIndex = GENERATOR.nextInt(collection.size());

    if (collection instanceof List<T> list) {
      return list.get(randomIndex);
    }

    return collection.stream().skip(randomIndex).findFirst().orElseThrow();
  }

  public static <T> List<T> newShuffledList(List<T> list) {
    var arrayList = new ArrayList<>(list);
    Collections.shuffle(arrayList, new Random(SEED));
    return List.copyOf(arrayList);
  }
}
