package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.model.PairingResult;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mebae.diparitor.utils.RandomUtils.*;

/**
 * Represents a complete assignment of players to powers for a whole tournament.
 * This includes all games and all pairings.
 */
final class GeneticTournament {
  private final ArrayList<GeneticGame> gameList;
  private final int gameCount;
  private final int powerCount;
  private final boolean hasPowerDifficulty;

  public GeneticTournament(List<GeneticGame> gameList, int powerCount, boolean hasPowerDifficulty) {
    this.gameList = new ArrayList<>(List.copyOf(gameList));
    this.gameCount = gameList.size();
    this.powerCount = powerCount;
    this.hasPowerDifficulty = hasPowerDifficulty;
  }

  public Set<Integer> computeSwappableGameIndexes(RegisteredPlayer player, int currentGameIndex) {
    return gameList.stream()
      .filter(game -> game.getIndex() == currentGameIndex || !game.containsPlayer(player))
      .mapToInt(GeneticGame::getIndex)
      .boxed()
      .collect(Collectors.toSet());
  }

  public GeneticTournament mutableCopyOf() {
    var copiedGameList = gameList.stream().map(GeneticGame::copyOf).toList();
    return new GeneticTournament(new ArrayList<>(copiedGameList), powerCount, hasPowerDifficulty);
  }

  public void computeRandomViablePlayerSwap() {
    var firstGameIndex = randomNumber(gameCount);
    var firstPlayerIndex = randomNumber(powerCount);
    var firstGame = gameList.get(firstGameIndex);
    var firstPlayer = firstGame.getPlayer(firstPlayerIndex);
    var firstSwappableGameIndexSet = computeSwappableGameIndexes(firstPlayer, firstGameIndex);
    int secondGameIndex, secondPlayerIndex;
    RegisteredPlayer secondPlayer;
    GeneticGame secondGame;

    while (true) {
      secondGameIndex = pickRandom(firstSwappableGameIndexSet);
      secondPlayerIndex = secondGameIndex == firstGameIndex
        ? randomNumberExcept(powerCount, firstPlayerIndex)
        : randomNumber(powerCount);
      secondGame = gameList.get(secondGameIndex);
      secondPlayer = secondGame.getPlayer(secondPlayerIndex);
      var secondSwappableGameIndexSet = computeSwappableGameIndexes(secondPlayer, secondGameIndex);
      if (secondSwappableGameIndexSet.contains(firstGameIndex)) {
        break;
      }
    }

    firstGame.setPlayer(firstPlayerIndex, secondPlayer);
    secondGame.setPlayer(secondPlayerIndex, firstPlayer);
  }

  public boolean isViable() {
    return gameList.stream().allMatch(GeneticGame::isViable);
  }

  public Map<RegisteredPlayer, List<Power>> computePlayerPowerList() {
    return gameList.stream()
      .flatMap(game -> game.computePairings().stream())
      .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
  }

  public Map<RegisteredPlayer, List<RegisteredPlayer>> computePlayerOpponentList() {
    return gameList.stream()
      .flatMap(game -> game.computeOpponents().stream())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> {
        existing.addAll(replacement);
        return existing;
      }));
  }

  public PairingResult toPairingResult() {
    return new PairingResult(gameList.stream().map(GeneticGame::computePairingsByPower).toList());
  }

  public boolean hasPowerDifficulty() {
    return hasPowerDifficulty;
  }

  @Override
  public String toString() {
    var gameLabelPrefix = "Game ";
    var gameLabelSuffix = " : ";
    var gameLabelLength = gameLabelPrefix.length() + gameLabelSuffix.length();
    var maxGameIndexLength = String.valueOf(gameCount).length();
    var maxGameLength = gameLabelLength + maxGameIndexLength;
    var maxNameLength = gameList.stream().mapToInt(GeneticGame::getPlayersMaxNameLength).max().orElseThrow();
    var powerLabel = "Power ";
    var maxPowerLength = powerLabel.length() + String.valueOf(powerCount).length();
    var cellWidth = Math.max(maxNameLength, maxPowerLength);
    var sb = new StringBuilder();

    sb.append(" ".repeat(maxGameLength));
    sb.append(IntStream.range(0, powerCount)
                .mapToObj(i -> String.format("%-" + cellWidth + "s", powerLabel + i))
                .collect(Collectors.joining(" | ")));
    sb.append("\n");

    sb.append(" ".repeat(maxGameLength));
    sb.append(IntStream.range(0, powerCount).mapToObj(i -> "-".repeat(cellWidth)).collect(Collectors.joining("-+-")));
    sb.append("\n");

    for (int gameIndex = 0; gameIndex < gameCount; gameIndex++) {
      var game = gameList.get(gameIndex);
      sb.append(gameLabelPrefix);
      sb.append(String.format("%-" + maxGameIndexLength + "d", gameIndex));
      sb.append(gameLabelSuffix);

      for (int i = 0; i < powerCount; i++) {
        if (i > 0) {
          sb.append(" | ");
        }
        sb.append(String.format("%-" + cellWidth + "s", game.getPlayer(i)));
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}
