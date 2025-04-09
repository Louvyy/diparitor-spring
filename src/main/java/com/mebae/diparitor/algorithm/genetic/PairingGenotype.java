package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.data.Power;
import com.mebae.diparitor.data.RegisteredPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a complete assignment of players to powers for a whole tournament.
 * This includes all games and all pairings.
 */
final class PairingGenotype {
  private final ArrayList<PairingChromosome> chromosomes;
  private final int genesCount;

  public PairingGenotype(ArrayList<PairingChromosome> chromosomes, int genesCount) {
    this.chromosomes = chromosomes;
    this.genesCount = genesCount;
  }

  public List<PairingChromosome> getChromosomes() {
    return List.copyOf(chromosomes);
  }

  public int getGenesCount() {
    return genesCount;
  }

  public List<Integer> getSwappableChromosomeIndexes(RegisteredPlayer player, int actualIndex) {
    return chromosomes.stream()
      .filter(game -> game.getIndex() == actualIndex || !game.containsPlayer(player))
      .mapToInt(PairingChromosome::getIndex)
      .boxed()
      .toList();
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

  @Override
  public String toString() {
    var sb = new StringBuilder();
    for (var gameIndex = 0; gameIndex < chromosomes.size(); gameIndex++) {
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
