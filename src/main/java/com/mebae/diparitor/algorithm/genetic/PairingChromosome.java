package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.data.Power;
import com.mebae.diparitor.data.RegisteredPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class PairingChromosome {
  private final int index;
  private final ArrayList<RegisteredPlayer> players;
  private final List<Power> powers;

  public PairingChromosome(int index, ArrayList<RegisteredPlayer> players, List<Power> powers) {
    this.index = index;
    this.players = players;
    this.powers = powers;
  }

  public boolean isViable() {
    return players.size() == players.stream().distinct().count();
  }

  public int getIndex() {
    return index;
  }

  public RegisteredPlayer getPlayer(int index) {
    return players.get(index);
  }

  public void setPlayer(int index, RegisteredPlayer player) {
    players.set(index, player);
  }

  public List<Map.Entry<RegisteredPlayer, Power>> getGenes() {
    return IntStream.range(0, players.size()).mapToObj(i -> Map.entry(players.get(i), powers.get(i))).toList();
  }

  public List<Map.Entry<RegisteredPlayer, ArrayList<RegisteredPlayer>>> getOpponents() {
    return players.stream()
      .map(registeredPlayer -> Map.entry(registeredPlayer, getPlayerOpponents(registeredPlayer)))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  private ArrayList<RegisteredPlayer> getPlayerOpponents(RegisteredPlayer player) {
    return players.stream()
      .filter(opponent -> !opponent.equals(player))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  public boolean containsPlayer(RegisteredPlayer player) {
    return players.contains(player);
  }

  @Override
  public String toString() {
    return players.stream().map(RegisteredPlayer::toString).collect(Collectors.joining("\n"));
  }
}
