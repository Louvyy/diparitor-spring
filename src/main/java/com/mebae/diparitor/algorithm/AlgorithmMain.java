package com.mebae.diparitor.algorithm;

import com.mebae.diparitor.algorithm.genetic.GeneticAlgorithm;
import com.mebae.diparitor.data.Power;
import com.mebae.diparitor.data.RegisteredPlayer;
import com.mebae.diparitor.data.TournamentSetup;

import java.util.Set;

public class AlgorithmMain {
  public static void main(String[] args) {
    // Arguments
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
    var algorithm = new GeneticAlgorithm();

    var tournamentSetup = TournamentSetup.of(players, powers);
    var tournament = algorithm.computeBestTournament(tournamentSetup);
    System.out.println(tournament);
  }
}
