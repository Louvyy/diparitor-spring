package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.algorithm.Algorithm;
import com.mebae.diparitor.data.Power;
import com.mebae.diparitor.data.RegisteredPlayer;
import com.mebae.diparitor.data.TournamentSetup;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.mebae.diparitor.utils.RandomUtils.*;
import static com.mebae.diparitor.utils.RandomUtils.getRandomNumber;

public final class GeneticAlgorithm implements Algorithm {
  @Override
  public List<Map<Power, RegisteredPlayer>> computeBestTournament(TournamentSetup tournamentSetup) {
    var randomPairingGenotype = randomInstanceOf(tournamentSetup);
    return List.of();
  }

  // TODO sortie acutelle
  private static PairingGenotype randomInstanceOf(TournamentSetup tournamentSetup) {
    Objects.requireNonNull(tournamentSetup);
    var players = tournamentSetup.getPlayers();
    var playerPerGameCount = tournamentSetup.getPlayerPerGameCount();
    var gamePairingList = generateRandomizedGamePairingList(players, playerPerGameCount);
    var powers = tournamentSetup.getPowers();
    var tournamentPairing = new PairingGenotype(gamePairingList, powers.size());
    while (!tournamentPairing.isViable()) {
      generateRandomParticipantSwap(tournamentPairing);
    }
    return tournamentPairing;
  }

  private static ArrayList<PairingChromosome> generateRandomizedGamePairingList(Set<RegisteredPlayer> players,
                                                                        int playerPerGameCount) {
    Objects.requireNonNull(players);
    var participantList = generateRandomizedParticipantList(players);
    return IntStream.range(0, participantList.size() / playerPerGameCount)
      .mapToObj(i -> new PairingChromosome(i,
                                           new ArrayList<>(participantList.subList(i * playerPerGameCount,
                                                                                   (i + 1) * playerPerGameCount))))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  private static ArrayList<RegisteredPlayer> generateRandomizedParticipantList(Set<RegisteredPlayer> players) {
    var participantList = players.stream()
      .flatMap(player -> Stream.generate(() -> player).limit(player.participationCount()))
      .collect(Collectors.toCollection(ArrayList::new));
    Collections.shuffle(participantList);
    return participantList;
  }

  private static void generateRandomParticipantSwap(PairingGenotype pairingGenotype) {
    var chromosomes = pairingGenotype.getChromosomes();
    var genesCount = pairingGenotype.getGenesCount();
    var firstRandomGameIndex = getRandomNumber(chromosomes.size());
    var firstRandomPlayerIndex = getRandomNumber(genesCount);
    var firstPlayer = chromosomes.get(firstRandomGameIndex).getGene(firstRandomPlayerIndex);
    var firstRandomGameViableIndexList = pairingGenotype.getSwappableChromosomeIndexes(firstPlayer,
                                                                                         firstRandomGameIndex);
    var secondRandomGameIndex = -1;
    var secondRandomPlayerIndex = -1;
    RegisteredPlayer secondPlayer = null;
    var secondPlayerViable = false;
    while (!secondPlayerViable) {
      secondRandomGameIndex = pickRandom(firstRandomGameViableIndexList);
      secondRandomPlayerIndex = secondRandomGameIndex == firstRandomGameIndex
        ? getRandomNumberExcept(genesCount, firstRandomPlayerIndex)
        : getRandomNumber(genesCount);
      secondPlayer = chromosomes.get(secondRandomGameIndex).getGene(secondRandomPlayerIndex);
      var secondRandomGameViableIndexList = pairingGenotype.getSwappableChromosomeIndexes(secondPlayer,
                                                                                            secondRandomGameIndex);
      if (secondRandomGameViableIndexList.contains(firstRandomGameIndex)) {
        secondPlayerViable = true;
      }
    }
    var firstGamePairing = chromosomes.get(firstRandomGameIndex);
    var secondGamePairing = chromosomes.get(secondRandomGameIndex);
    firstGamePairing.setGene(firstRandomPlayerIndex, secondPlayer);
    secondGamePairing.setGene(secondRandomPlayerIndex, firstPlayer);
  }

  // TODO DEBUG A DEPLACER DANS TESTS
  public static void main(String[] args) {
    var players = Set.of(new RegisteredPlayer("Alice", 3),
                         new RegisteredPlayer("Bob", 1),
                         new RegisteredPlayer("Charlie", 4),
                         new RegisteredPlayer("Diana", 3),
                         new RegisteredPlayer("Eve", 3),
                         new RegisteredPlayer("Frank", 4),
                         new RegisteredPlayer("Grace", 3),
                         new RegisteredPlayer("Hilda", 2),
                         new RegisteredPlayer("Irma", 3),
                         new RegisteredPlayer("Jodie", 4),
                         new RegisteredPlayer("Killian", 4),
                         new RegisteredPlayer("Laura", 2),
                         new RegisteredPlayer("Martin", 4),
                         new RegisteredPlayer("Nadia", 2));

    var powers = Set.of(new Power("Austria-Hungary", 2.78),
                        new Power("France", 6.14),
                        new Power("Germany", 5.56),
                        new Power("Great Britain", 4.19),
                        new Power("Italy", 5.60),
                        new Power("Russia", 5.18),
                        new Power("Turkey", 4.56));
    var tournamentSetup = TournamentSetup.of(players, powers);
    var longer = -1.0;
    var shorter = Double.MAX_VALUE;
    var startSimulation = System.nanoTime();
    var iterations = 100_000;
    for (int i = 0; i < iterations; i++) {
      var start = System.nanoTime();
      var tournamentPairing = GeneticAlgorithm.randomInstanceOf(tournamentSetup);
      var end = System.nanoTime();
      var durationInNanoseconds = end - start;
      var durationInMilliseconds = durationInNanoseconds / 1_000_000.0;
      if (durationInMilliseconds < shorter) {
        shorter = durationInMilliseconds;
      }
      if (durationInMilliseconds > longer) {
        longer = durationInMilliseconds;
      }
      System.out.println("Durée : " + durationInMilliseconds + " ms (" + i + " / "+ iterations + ")");
    }
    var endSimulation = System.nanoTime();
    var simulationDurationInNanoseconds = endSimulation - startSimulation;
    var simulationDurationInSeconds = simulationDurationInNanoseconds / 1_000_000_000.0;
    System.out.println("Temps le plus long : " + longer + " ms");
    System.out.println("Temps le plus court : " + shorter + " ms");
    System.out.println("Moyenne : " + simulationDurationInNanoseconds / 1_000_000.0 / iterations + " ms");
    System.out.println("Temps total : " + simulationDurationInSeconds + " s");
  }
}
