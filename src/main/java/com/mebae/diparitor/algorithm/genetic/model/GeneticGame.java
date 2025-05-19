package com.mebae.diparitor.algorithm.genetic.model;

import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO JAVADOC
public final class GeneticGame {
  private final List<RegisteredPlayer> players;
  private final List<Power> powers;
  private Set<RegisteredPlayer> playerSet = null;

  public GeneticGame(List<RegisteredPlayer> players, List<Power> powers) {
    this.players = List.copyOf(players);
    this.powers = List.copyOf(powers);
  }

  public boolean isViable() {
    return players.size() == players.stream().distinct().count();
  }

  public GeneticGame withReplacedPlayer(int playerIndex, RegisteredPlayer newPlayer) {
    var newPlayers = new ArrayList<>(players);
    newPlayers.set(playerIndex, newPlayer);
    return new GeneticGame(newPlayers, powers);
  }

  public RegisteredPlayer getPlayer(int index) {
    return players.get(index);
  }

  public List<Map.Entry<RegisteredPlayer, Power>> computePairings() {
    return IntStream.range(0, players.size())
        .mapToObj(i -> Map.entry(players.get(i), powers.get(i))).toList();
  }

  public Map<Power, RegisteredPlayer> computePairingsByPower() {
    return IntStream.range(0, players.size()).boxed()
        .collect(Collectors.toMap(powers::get, players::get));
  }

  public List<Map.Entry<RegisteredPlayer, List<RegisteredPlayer>>> computeOpponents() {
    return players.stream()
        .map(registeredPlayer -> Map.entry(registeredPlayer, getPlayerOpponents(registeredPlayer)))
        .toList();
  }

  public int getPlayersMaxNameLength() {
    return players.stream().mapToInt(player -> player.name().length()).max().orElseThrow();
  }

  private List<RegisteredPlayer> getPlayerOpponents(RegisteredPlayer player) {
    return players.stream().filter(opponent -> !opponent.equals(player)).toList();
  }

  public boolean containsPlayer(RegisteredPlayer player) {
    if (playerSet == null) {
      playerSet = Set.copyOf(players);
    }
    return playerSet.contains(player);
  }

  @Override
  public String toString() {
    return players.stream().map(RegisteredPlayer::toString).collect(Collectors.joining(", "));
  }
}
