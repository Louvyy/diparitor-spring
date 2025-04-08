package com.mebae.diparitor.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public final class RandomUtils {
  public static int getRandomNumber(int upperBound) {
    if (upperBound < 1) {
      throw new IllegalArgumentException("Upper bound must be at least 0 to get a number");
    }
    return ThreadLocalRandom.current().nextInt(upperBound);
  }

  public static int getRandomNumberExcept(int upperBound, int exception) {
    if (upperBound < 2) {
      throw new IllegalArgumentException("Upper bound must be at least 1 to get a number");
    }
    return pickRandom(IntStream.range(0, upperBound).filter(x -> x != exception).boxed().toList());
  }

  public static <T> T pickRandom(List<T> list) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException("Cannot pick from an empty list.");
    }
    return list.get(ThreadLocalRandom.current().nextInt(list.size()));
  }
}
