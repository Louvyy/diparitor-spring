package com.mebae.diparitor.algorithm;

import com.mebae.diparitor.algorithm.genetic.GeneticAlgorithm;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import com.mebae.diparitor.model.TournamentSetup;

import java.util.Set;

public class Main {
  public static void main(String[] args) {
    // Arguments
    var players = Set.of(new RegisteredPlayer("Alice", 4),
                         new RegisteredPlayer("Bob", 3),
                         new RegisteredPlayer("Charlie", 5),
                         new RegisteredPlayer("Diana", 4),
                         new RegisteredPlayer("Eve", 4),
                         new RegisteredPlayer("Frank", 5),
                         new RegisteredPlayer("Grace", 6),
                         new RegisteredPlayer("Hilda", 2),
                         new RegisteredPlayer("Irma", 3),
                         new RegisteredPlayer("Jodie", 4),
                         new RegisteredPlayer("Killian", 4),
                         new RegisteredPlayer("Laura", 3),
                         new RegisteredPlayer("Martin", 5),
                         new RegisteredPlayer("Nadia", 2),
                         new RegisteredPlayer("Olivia", 5),
                         new RegisteredPlayer("Paul", 3),
                         new RegisteredPlayer("Quill", 1),
                         new RegisteredPlayer("Romy", 3),
                         new RegisteredPlayer("Sacha", 4),
                         new RegisteredPlayer("Tristan", 3),
                         new RegisteredPlayer("Ulysse", 4));
    var powers = Set.of(new Power("Austria-Hungary", 2.78),
                        new Power("France", 6.14),
                        new Power("Germany", 5.56),
                        new Power("Great Britain", 4.19),
                        new Power("Italy", 5.60),
                        new Power("Russia", 5.18),
                        new Power("Turkey", 4.56));
    var hasPowerDifficulty = true;
    var algorithm = new GeneticAlgorithm();
    var tournamentSetup = TournamentSetup.of(players, powers, hasPowerDifficulty);
    var tournament = algorithm.computeBestTournament(tournamentSetup);
    System.out.println(tournament);
  }
}
