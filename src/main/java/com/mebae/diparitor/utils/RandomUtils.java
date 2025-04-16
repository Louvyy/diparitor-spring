package com.mebae.diparitor.utils;

import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public final class RandomUtils {
  public static int randomNumber(int upperBound) {
    if (upperBound < 1) {
      throw new IllegalArgumentException("Upper bound must be at least 0 to get a number");
    }
    return ThreadLocalRandom.current().nextInt(upperBound);
  }

  public static int randomNumberExcept(int upperBound, int exception) {
    if (upperBound < 2) {
      throw new IllegalArgumentException("Upper bound must be at least 1 to get a number");
    }
    return pickRandom(IntStream.range(0, upperBound).filter(x -> x != exception).boxed().toList());
  }

  public static <T> T pickRandom(Collection<T> collection) {
    if (collection.isEmpty()) {
      throw new IllegalArgumentException("Cannot pick from an empty collection");
    }

    var randomIndex = ThreadLocalRandom.current().nextInt(collection.size());

    if (collection instanceof RandomAccess) {
      return ((List<T>) collection).get(randomIndex);
    }

    for (var element : collection) {
      if (randomIndex == 0) {
        return element;
      }
      randomIndex--;
    }

    throw new IllegalStateException("Unreachable");
  }
}
