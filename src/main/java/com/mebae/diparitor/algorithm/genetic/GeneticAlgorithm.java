package com.mebae.diparitor.algorithm.genetic;

import com.mebae.diparitor.algorithm.Algorithm;
import com.mebae.diparitor.data.Power;
import com.mebae.diparitor.data.RegisteredPlayer;
import com.mebae.diparitor.data.TournamentSetup;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class GeneticAlgorithm implements Algorithm {
  @Override
  public List<Map<Power, RegisteredPlayer>> computeBestTournament(TournamentSetup tournamentSetup) {
    var bestGenoype = randomInstanceOf(tournamentSetup);
    var bestFitness = FitnessEvaluator.evaluate(bestGenoype, true);
    System.out.println("Avant :");
    System.out.println(bestFitness);
    PairingGenotype mutatedGenotype;
    FitnessScore mutatedFitness;
    var notViableCount = 0;
    var maxNotViableCount = 500_000; // TODO définir une valeur cohérente par rapport au jeu de données
    while (notViableCount < maxNotViableCount) {
      mutatedGenotype = bestGenoype.copyOf();
      mutatedGenotype.computeRandomViablePlayerSwap();
      mutatedFitness = FitnessEvaluator.evaluate(mutatedGenotype, true);
      if (mutatedFitness.isBetterThan(bestFitness)) {
        bestGenoype = mutatedGenotype.copyOf();
        bestFitness = mutatedFitness;
      }
      else {
        notViableCount++;
      }
    }
    System.out.println("Après :");
    System.out.println(bestFitness);
    return bestGenoype.getGenotype();
  }

  private static PairingGenotype randomInstanceOf(TournamentSetup tournamentSetup) {
    Objects.requireNonNull(tournamentSetup);
    var players = tournamentSetup.getPlayers();
    var powers = tournamentSetup.getPowers().stream().toList();
    var gamePairingList = generateRandomizedGamePairingList(players, powers);
    var tournamentPairing = new PairingGenotype(gamePairingList, powers.size());
    while (!tournamentPairing.isViable()) {
      tournamentPairing.computeRandomViablePlayerSwap();
    }
    return tournamentPairing;
  }

  private static ArrayList<PairingChromosome> generateRandomizedGamePairingList(Set<RegisteredPlayer> players,
                                                                                List<Power> powers) {
    Objects.requireNonNull(players);
    var participantList = generateRandomizedParticipantList(players);
    var geneCount = powers.size();
    return IntStream.range(0, participantList.size() / geneCount)
      .mapToObj(i -> new PairingChromosome(i,
                                           new ArrayList<>(participantList.subList(i * geneCount, (i + 1) * geneCount)),
                                           powers))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  private static ArrayList<RegisteredPlayer> generateRandomizedParticipantList(Set<RegisteredPlayer> players) {
    var participantList = players.stream()
      .flatMap(player -> Stream.generate(() -> player).limit(player.participationCount()))
      .collect(Collectors.toCollection(ArrayList::new));
    Collections.shuffle(participantList);
    return participantList;
  }

  // TODO DEBUG A DEPLACER DANS TESTS
//  public static void main(String[] args) {
//    var players = Set.of(new RegisteredPlayer("Alice", 3),
//                         new RegisteredPlayer("Bob", 1),
//                         new RegisteredPlayer("Charlie", 4),
//                         new RegisteredPlayer("Diana", 3),
//                         new RegisteredPlayer("Eve", 3),
//                         new RegisteredPlayer("Frank", 4),
//                         new RegisteredPlayer("Grace", 3),
//                         new RegisteredPlayer("Hilda", 2),
//                         new RegisteredPlayer("Irma", 3),
//                         new RegisteredPlayer("Jodie", 4),
//                         new RegisteredPlayer("Killian", 4),
//                         new RegisteredPlayer("Laura", 2),
//                         new RegisteredPlayer("Martin", 4),
//                         new RegisteredPlayer("Nadia", 2));
//
//    var powers = Set.of(new Power("Austria-Hungary", 2.78),
//                        new Power("France", 6.14),
//                        new Power("Germany", 5.56),
//                        new Power("Great Britain", 4.19),
//                        new Power("Italy", 5.60),
//                        new Power("Russia", 5.18),
//                        new Power("Turkey", 4.56));
//    var tournamentSetup = TournamentSetup.of(players, powers);
//    var longer = -1.0;
//    var shorter = Double.MAX_VALUE;
//    var startSimulation = System.nanoTime();
//    var iterations = 100_000;
//    for (int i = 0; i < iterations; i++) {
//      var start = System.nanoTime();
//      var tournamentPairing = GeneticAlgorithm.randomInstanceOf(tournamentSetup);
//      var end = System.nanoTime();
//      var durationInNanoseconds = end - start;
//      var durationInMilliseconds = durationInNanoseconds / 1_000_000.0;
//      if (durationInMilliseconds < shorter) {
//        shorter = durationInMilliseconds;
//      }
//      if (durationInMilliseconds > longer) {
//        longer = durationInMilliseconds;
//      }
//      System.out.println("Durée : " + durationInMilliseconds + " ms (" + i + " / " + iterations + ")");
//    }
//    var endSimulation = System.nanoTime();
//    var simulationDurationInNanoseconds = endSimulation - startSimulation;
//    var simulationDurationInSeconds = simulationDurationInNanoseconds / 1_000_000_000.0;
//    System.out.println("Temps le plus long : " + longer + " ms");
//    System.out.println("Temps le plus court : " + shorter + " ms");
//    System.out.println("Moyenne : " + simulationDurationInNanoseconds / 1_000_000.0 / iterations + " ms");
//    System.out.println("Temps total : " + simulationDurationInSeconds + " s");
//  }
}
