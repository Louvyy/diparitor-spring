package com.mebae.diparitor.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PairingResult {
  private final List<Map<Power, RegisteredPlayer>> result;

  public PairingResult(List<Map<Power, RegisteredPlayer>> result) {
    this.result = List.copyOf(result);
  }

  private int playerMaxNameLength() {
    return result.stream()
      .flatMap(game -> game.values().stream())
      .map(player -> player.name().length())
      .max(Integer::compareTo)
      .orElseThrow();
  }

  private int powerMaxNameLength() {
    return result.stream()
      .flatMap(game -> game.keySet().stream())
      .map(power -> power.name().length())
      .max(Integer::compareTo)
      .orElseThrow();
  }

  @Override
  public String toString() {
    var cellWidth = Math.max(playerMaxNameLength(), powerMaxNameLength());
    var sortedPowers = result.getFirst().keySet().stream().sorted(Comparator.comparing(Power::toString)).toList();
    var sb = new StringBuilder();

    // Header line
    sb.append(" ".repeat("Game XX: ".length())); // pour aligner avec "Game X:"
    sb.append(sortedPowers.stream()
                .map(p -> String.format("%-" + cellWidth + "s", p.name()))
                .collect(Collectors.joining(" | ")));
    sb.append("\n");

    // Separator
    sb.append(" ".repeat("Game XX: ".length()));
    sb.append(sortedPowers.stream().map(p -> "-".repeat(cellWidth)).collect(Collectors.joining("-+-")));
    sb.append("\n");

    // Body
    for (var i = 0; i < result.size(); i++) {
      var game = result.get(i);
      sb.append(String.format("Game %-2d: ", i));
      sb.append(sortedPowers.stream()
                  .map(power -> String.format("%-" + cellWidth + "s", game.get(power).name()))
                  .collect(Collectors.joining(" | ")));
      sb.append("\n");
    }

    return sb.toString();
  }
}
