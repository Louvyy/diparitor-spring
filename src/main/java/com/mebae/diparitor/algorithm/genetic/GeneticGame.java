package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class GeneticGame {
  private final int index;
  private final ArrayList<RegisteredPlayer> players;
  private final List<Power> powers;

  public GeneticGame(int index, List<RegisteredPlayer> players, List<Power> powers) {
    if (index < 0) {
      throw new IllegalArgumentException("Index cannot be negative");
    }
    this.index = index;
    this.players = new ArrayList<>(List.copyOf(players));
    this.powers = List.copyOf(powers);
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

  public List<Map.Entry<RegisteredPlayer, Power>> computePairings() {
    return IntStream.range(0, players.size()).mapToObj(i -> Map.entry(players.get(i), powers.get(i))).toList();
  }

  public Map<Power, RegisteredPlayer> computePairingsByPower() {
    return IntStream.range(0, players.size())
      .boxed()
      .collect(Collectors.toMap(powers::get, players::get));
  }

  public List<Map.Entry<RegisteredPlayer, ArrayList<RegisteredPlayer>>> computeOpponents() {
    return players.stream()
      .map(registeredPlayer -> Map.entry(registeredPlayer, getPlayerOpponents(registeredPlayer)))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  private ArrayList<RegisteredPlayer> getPlayerOpponents(RegisteredPlayer player) {
    return players.stream()
      .filter(opponent -> !opponent.equals(player))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  public GeneticGame copyOf() {
    return new GeneticGame(index, players, powers);
  }

  public boolean containsPlayer(RegisteredPlayer player) {
    return players.contains(player);
  }

  @Override
  public String toString() {
    return players.stream().map(RegisteredPlayer::toString).collect(Collectors.joining("\n"));
  }
}
