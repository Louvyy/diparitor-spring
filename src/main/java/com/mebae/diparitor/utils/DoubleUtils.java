package com.mebae.diparitor.utils;

import java.util.List;

public final class DoubleUtils {
  public static double computeAverage(List<Double> values) {
    return values.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
  }

  public static double computeVariance(List<Double> values) {
    var mean = computeAverage(values);
    return values.stream()
      .mapToDouble(v -> Math.pow(v - mean, 2))
      .average()
      .orElse(0.0); // pour être plus tolérant que `orElseThrow`
  }
}
