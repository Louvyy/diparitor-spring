package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.data.Power;
import com.mebae.diparitor.data.RegisteredPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mebae.diparitor.utils.RandomUtils.*;
import static com.mebae.diparitor.utils.RandomUtils.getRandomNumber;

/**
 * Represents a complete assignment of players to powers for a whole tournament.
 * This includes all games and all pairings.
 */
final class PairingGenotype {
  private final ArrayList<PairingChromosome> chromosomes;
  private final int chromosomeCount;
  private final int genesCount;

  public PairingGenotype(ArrayList<PairingChromosome> chromosomes, int genesCount) {
    this.chromosomes = chromosomes;
    this.chromosomeCount = chromosomes.size();
    this.genesCount = genesCount;
  }

  public List<Integer> getSwappableChromosomeIndexes(RegisteredPlayer player, int actualIndex) {
    return chromosomes.stream()
      .filter(game -> game.getIndex() == actualIndex || !game.containsPlayer(player))
      .mapToInt(PairingChromosome::getIndex)
      .boxed()
      .toList();
  }

  public PairingGenotype copyOf() {
    return new PairingGenotype(new ArrayList<>(List.copyOf(chromosomes)), genesCount);
  }

  public void computeRandomViablePlayerSwap() {
    var firstRandomChromosomeIndex = getRandomNumber(chromosomeCount);
    var firstRandomPlayerIndex = getRandomNumber(genesCount);
    var firstPlayer = chromosomes.get(firstRandomChromosomeIndex).getPlayer(firstRandomPlayerIndex);
    var firstRandomGameViableIndexList = getSwappableChromosomeIndexes(firstPlayer,
                                                                                       firstRandomChromosomeIndex);
    var secondRandomGameIndex = -1;
    var secondRandomPlayerIndex = -1;
    RegisteredPlayer secondPlayer = null;
    var secondPlayerViable = false;
    while (!secondPlayerViable) {
      secondRandomGameIndex = pickRandom(firstRandomGameViableIndexList);
      secondRandomPlayerIndex = secondRandomGameIndex == firstRandomChromosomeIndex
        ? getRandomNumberExcept(genesCount,
                                firstRandomPlayerIndex)
        : getRandomNumber(genesCount);
      secondPlayer = chromosomes.get(secondRandomGameIndex).getPlayer(secondRandomPlayerIndex);
      var secondRandomGameViableIndexList = getSwappableChromosomeIndexes(secondPlayer,
                                                                                          secondRandomGameIndex);
      if (secondRandomGameViableIndexList.contains(firstRandomChromosomeIndex)) {
        secondPlayerViable = true;
      }
    }
    var firstGamePairing = chromosomes.get(firstRandomChromosomeIndex);
    var secondGamePairing = chromosomes.get(secondRandomGameIndex);
    firstGamePairing.setPlayer(firstRandomPlayerIndex, secondPlayer);
    secondGamePairing.setPlayer(secondRandomPlayerIndex, firstPlayer);
  }

  public boolean isViable() {
    return chromosomes.stream().allMatch(PairingChromosome::isViable);
  }

  public Map<RegisteredPlayer, List<Power>> computePlayerPowerList() {
    return chromosomes.stream()
      .flatMap(chromosome -> chromosome.getGenes().stream())
      .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
  }

  public Map<RegisteredPlayer, List<RegisteredPlayer>> computePlayerOpponentList() {
    return chromosomes.stream()
      .flatMap(chromosome -> chromosome.getOpponents().stream())
      .collect(Collectors.toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (existing, replacement) -> {
                                  existing.addAll(replacement);
                                  return existing;
                                }));
  }

  public List<Map<Power, RegisteredPlayer>> getGenotype() {
    return chromosomes.stream().map(PairingChromosome::getMappedGenes).toList();
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    for (var gameIndex = 0; gameIndex < chromosomeCount; gameIndex++) {
      sb.append("Game ").append(gameIndex).append(":\n");
      for (var powerIndex = 0; powerIndex < genesCount; powerIndex++) {
        sb.append("Power ")
          .append(powerIndex)
          .append(" -> ")
          .append(chromosomes.get(gameIndex).getPlayer(powerIndex))
          .append("\n");
      }
      sb.append("---------------------------------------------\n");
    }
    return sb.toString();
  }
}
