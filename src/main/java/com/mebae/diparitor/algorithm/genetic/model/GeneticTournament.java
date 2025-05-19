package com.mebae.diparitor.algorithm.genetic.model;

import static com.mebae.diparitor.utils.RandomUtils.pickRandom;
import static com.mebae.diparitor.utils.RandomUtils.randomNonNegativeNumber;
import static com.mebae.diparitor.utils.RandomUtils.randomNonNegativeNumberExcept;

import com.mebae.diparitor.model.PairingResult;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO JAVADOC
/**
 * Represents a complete assignment of players to powers for a whole tournament.
 * This includes all games and all pairings.
 */
public final class GeneticTournament {
  private final List<GeneticGame> gameList;
  private final int gameCount;
  private final int powerCount;
  private final boolean hasPowerDifficulty;

  public GeneticTournament(List<GeneticGame> gameList, int powerCount, boolean hasPowerDifficulty) {
    this.gameList = List.copyOf(gameList);
    this.gameCount = gameList.size();
    this.powerCount = powerCount;
    this.hasPowerDifficulty = hasPowerDifficulty;
  }

  public Set<Integer> computeSwappableGameIndexes(RegisteredPlayer player, int currentGameIndex) {
    return IntStream.range(0, gameList.size())
        .filter(i -> i == currentGameIndex || !gameList.get(i).containsPlayer(player)).boxed()
        .collect(Collectors.toSet());
  }

  public GeneticTournament computeRandomViablePlayerSwap() {
    var firstGameIndex = randomNonNegativeNumber(gameCount);
    var firstPlayerIndex = randomNonNegativeNumber(powerCount);
    var firstGame = gameList.get(firstGameIndex);
    var firstPlayer = firstGame.getPlayer(firstPlayerIndex);
    var firstSwappableGameIndexSet = computeSwappableGameIndexes(firstPlayer, firstGameIndex);
    int secondGameIndex;
    int secondPlayerIndex;
    RegisteredPlayer secondPlayer;
    GeneticGame secondGame;

    while (true) {
      secondGameIndex = pickRandom(firstSwappableGameIndexSet);
      secondPlayerIndex =
          secondGameIndex == firstGameIndex ? randomNonNegativeNumberExcept(powerCount,
                                                                            firstPlayerIndex)
              : randomNonNegativeNumber(powerCount);
      secondGame = gameList.get(secondGameIndex);
      secondPlayer = secondGame.getPlayer(secondPlayerIndex);
//      if (firstGameIndex == secondGameIndex || !firstGame.containsPlayer(secondPlayer)) {
//        break;
//      }
      var secondSwappableGameIndexSet = computeSwappableGameIndexes(secondPlayer, secondGameIndex);
      if (secondSwappableGameIndexSet.contains(firstGameIndex)) {
        break;
      }
    }

    if (firstGameIndex == secondGameIndex) {
      firstGame = firstGame.withReplacedPlayer(firstPlayerIndex, secondPlayer);
      firstGame = firstGame.withReplacedPlayer(secondPlayerIndex, firstPlayer);
      return withReplacedGame(firstGameIndex, firstGame);
    }
    firstGame = firstGame.withReplacedPlayer(firstPlayerIndex, secondPlayer);
    secondGame = secondGame.withReplacedPlayer(secondPlayerIndex, firstPlayer);
    return withReplacedGames(firstGameIndex, firstGame, secondGameIndex, secondGame);
  }

  private GeneticTournament withReplacedGames(int index1, GeneticGame game1, int index2,
                                              GeneticGame game2) {
    var newGameList = new ArrayList<>(gameList);
    newGameList.set(index1, game1);
    newGameList.set(index2, game2);
    return new GeneticTournament(newGameList, powerCount, hasPowerDifficulty);
  }

  private GeneticTournament withReplacedGame(int index1, GeneticGame game1) {
    var newGameList = new ArrayList<>(gameList);
    newGameList.set(index1, game1);
    return new GeneticTournament(newGameList, powerCount, hasPowerDifficulty);
  }

  /**
   *
   * @return true if all games are individually viable
   */
  public boolean isViable() {
    return gameList.stream().allMatch(GeneticGame::isViable);
  }

  public Map<RegisteredPlayer, List<Power>> computePlayerPowerList() {
    return gameList.stream().flatMap(game -> game.computePairings().stream()).collect(
        Collectors.groupingBy(Map.Entry::getKey,
                              Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
  }

  public Map<RegisteredPlayer, List<RegisteredPlayer>> computePlayerOpponentList() {
    return gameList.stream().flatMap(game -> game.computeOpponents().stream()).collect(
        Collectors.toMap(Map.Entry::getKey, entry -> new ArrayList<>(entry.getValue()),
                         (existing, replacement) -> {
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
    var maxNameLength =
        gameList.stream().mapToInt(GeneticGame::getPlayersMaxNameLength).max().orElseThrow();
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
    sb.append(IntStream.range(0, powerCount).mapToObj(i -> "-".repeat(cellWidth))
                  .collect(Collectors.joining("-+-")));
    sb.append("\n");

    for (int gameIndex = 0; gameIndex < gameCount; gameIndex++) {
      sb.append(gameLabelPrefix);
      sb.append(String.format("%-" + maxGameIndexLength + "d", gameIndex));
      sb.append(gameLabelSuffix);
      var game = gameList.get(gameIndex);

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
