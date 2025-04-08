package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.data.RegisteredPlayer;

import java.util.ArrayList;
import java.util.stream.Collectors;

final class PairingChromosome {
  private final int index;
  private final ArrayList<RegisteredPlayer> genes;

  public PairingChromosome(int index, ArrayList<RegisteredPlayer> genes) {
    this.index = index;
    this.genes = genes;
  }

  public boolean isViable() {
    return genes.size() == genes.stream().distinct().count();
  }

  public int getIndex() {
    return index;
  }

  public RegisteredPlayer getGene(int index) {
    return genes.get(index);
  }

  public void setGene(int index, RegisteredPlayer player) {
    genes.set(index, player);
  }

  public boolean containsGene(RegisteredPlayer player) {
    return genes.contains(player);
  }

  @Override
  public String toString() {
    return genes.stream().map(RegisteredPlayer::toString).collect(Collectors.joining("\n"));
  }
}
