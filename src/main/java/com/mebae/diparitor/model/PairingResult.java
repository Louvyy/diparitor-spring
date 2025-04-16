package com.mebae.diparitor.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PairingResult {
  private final List<Map<Power, RegisteredPlayer>> result;
  private final int gameCount;

  public PairingResult(List<Map<Power, RegisteredPlayer>> result) {
    this.result = List.copyOf(result);
    this.gameCount = result.size();
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
    var gameLabelPrefix = "Game ";
    var gameLabelSuffix = " : ";
    var gameLabelLength = gameLabelPrefix.length() + gameLabelSuffix.length();
    var maxGameIndexLength = String.valueOf(gameCount).length();
    var maxGameLength = gameLabelLength + maxGameIndexLength; // "Game XX : "
    var cellWidth = Math.max(playerMaxNameLength(), powerMaxNameLength());
    var sortedPowers = result.getFirst().keySet().stream().sorted(Comparator.comparing(Power::toString)).toList();
    var sb = new StringBuilder();

    sb.append(" ".repeat(maxGameLength));
    sb.append(sortedPowers.stream()
                .map(p -> String.format("%-" + cellWidth + "s", p.name()))
                .collect(Collectors.joining(" | ")));
    sb.append("\n");

    sb.append(" ".repeat(maxGameLength));
    sb.append(sortedPowers.stream().map(p -> "-".repeat(cellWidth)).collect(Collectors.joining("-+-")));
    sb.append("\n");

    for (var i = 0; i < gameCount; i++) {
      var game = result.get(i);
      sb.append(gameLabelPrefix)
        .append(String.format("%-" + maxGameIndexLength + "d", i))
        .append(gameLabelSuffix);

      for (int j = 0; j < sortedPowers.size(); j++) {
        if (j > 0) sb.append(" | ");
        var player = game.get(sortedPowers.get(j));
        sb.append(String.format("%-" + cellWidth + "s", player.name()));
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}
