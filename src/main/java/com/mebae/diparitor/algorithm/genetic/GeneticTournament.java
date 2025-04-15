package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.model.PairingResult;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

  public List<Integer> computeSwappableGameIndexes(RegisteredPlayer player, int actualIndex) {
    return gameList.stream()
      .filter(game -> game.getIndex() == actualIndex || !game.containsPlayer(player))
      .mapToInt(GeneticGame::getIndex)
      .boxed()
      .toList();
  }

  public GeneticTournament mutableCopyOf() {
    var copiedGameList = gameList.stream().map(GeneticGame::copyOf).toList();
    return new GeneticTournament(new ArrayList<>(copiedGameList), powerCount, hasPowerDifficulty);
  }

  public void computeRandomViablePlayerSwap() {
    var firstRandomGameIndex = randomNumber(gameCount);
    var firstRandomPlayerIndex = randomNumber(powerCount);
    var firstPlayer = gameList.get(firstRandomGameIndex).getPlayer(firstRandomPlayerIndex);
    var firstRandomGameViableIndexList = computeSwappableGameIndexes(firstPlayer, firstRandomGameIndex);
    int secondRandomGameIndex, secondRandomPlayerIndex;
    RegisteredPlayer secondPlayer;
    var secondPlayerViable = false;

    // TODO précalculer les listes (rien n'est mutable dans la boucle)
    // Map<Integer, List> map.get(secondRandomGameIndex)
    do {
      secondRandomGameIndex = pickRandom(firstRandomGameViableIndexList);
      secondRandomPlayerIndex = secondRandomGameIndex == firstRandomGameIndex
        ? randomNumberExcept(powerCount,
                             firstRandomPlayerIndex)
        : randomNumber(powerCount);
      secondPlayer = gameList.get(secondRandomGameIndex).getPlayer(secondRandomPlayerIndex);
      var secondRandomGameViableIndexList = computeSwappableGameIndexes(secondPlayer, secondRandomGameIndex); // O(n²)
      if (secondRandomGameViableIndexList.contains(firstRandomGameIndex)) { // contains dans une list AAAAAA
        secondPlayerViable = true;
      }
    } while (!secondPlayerViable);

    var firstGamePairing = gameList.get(firstRandomGameIndex);
    var secondGamePairing = gameList.get(secondRandomGameIndex);
    firstGamePairing.setPlayer(firstRandomPlayerIndex, secondPlayer);
    secondGamePairing.setPlayer(secondRandomPlayerIndex, firstPlayer);
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
    var maxNameLength = gameList.stream()
      .flatMap(game -> IntStream.range(0, powerCount).mapToObj(i -> game.getPlayer(i).toString()))
      .mapToInt(String::length)
      .max()
      .orElse(10);

    var cellWidth = Math.max(maxNameLength, 7); // au moins assez large pour les noms des puissances

    // Ligne d'en-tête des puissances (Power 0, Power 1, etc.)
    var header = " ".repeat("Game XX: ".length()) + IntStream.range(0, powerCount)
      .mapToObj(i -> String.format("%-" + cellWidth + "s", "Power " + i))
      .collect(Collectors.joining(" | "));

    // Ligne de séparation
    var separator = " ".repeat("Game XX: ".length()) + IntStream.range(0, powerCount)
      .mapToObj(i -> "-".repeat(cellWidth))
      .collect(Collectors.joining("-+-"));

    // Corps : chaque ligne représente une partie
    var body = IntStream.range(0, gameCount).mapToObj(gameIndex -> {
      var game = gameList.get(gameIndex);
      String playersLine = IntStream.range(0, powerCount)
        .mapToObj(i -> String.format("%-" + cellWidth + "s", game.getPlayer(i)))
        .collect(Collectors.joining(" | "));
      return String.format("Game %-2d: %s", gameIndex, playersLine);
    }).collect(Collectors.joining("\n"));

    return header + "\n" + separator + "\n" + body;
  }
}
