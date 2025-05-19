import com.mebae.diparitor.algorithm.genetic.GeneticDiplomacyAlgorithm;
import com.mebae.diparitor.algorithm.genetic.initializer.ShuffledOrderedDistributionGenerator;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import com.mebae.diparitor.model.TournamentSetup;

// TODO CHECKSTYLE nouveau main()
// TODO CHECKSTYLE ne pas autoriser les TODO (en tout cas sur une branche de version ou main)
// TODO CHECKSTYLE sur tous les fichiers + Github Actions
// TODO TESTS sur toutes les méthodes de tous les fichiers (ou presque)

void main() {
  //   Arguments
  var players = Set.of(new RegisteredPlayer("Alice", 4), new RegisteredPlayer("Bob", 3),
      new RegisteredPlayer("Charlie", 5), new RegisteredPlayer("Diana", 4),
      new RegisteredPlayer("Eve", 4), new RegisteredPlayer("Frank", 5),
      new RegisteredPlayer("Grace", 6), new RegisteredPlayer("Hilda", 2),
      new RegisteredPlayer("Irma", 3), new RegisteredPlayer("Jodie", 4),
      new RegisteredPlayer("Killian", 4), new RegisteredPlayer("Laura", 3),
      new RegisteredPlayer("Martin", 5), new RegisteredPlayer("Nadia", 2),
      new RegisteredPlayer("Olivia", 5), new RegisteredPlayer("Paul", 3),
      new RegisteredPlayer("Quill", 1), new RegisteredPlayer("Romy", 3),
      new RegisteredPlayer("Sacha", 4), new RegisteredPlayer("Tristan", 3),
      new RegisteredPlayer("Ulysse", 4));
  var powers =
      Set.of(Power.withDifficulty("Austria-Hungary", 2.78), Power.withDifficulty("France", 6.14),
          Power.withDifficulty("Germany", 5.56), Power.withDifficulty("Great Britain", 4.19),
          Power.withDifficulty("Italy", 5.60), Power.withDifficulty("Russia", 5.18),
          Power.withDifficulty("Turkey", 4.56));
  var hasPowerDifficulty = true;
  var algorithm = new GeneticDiplomacyAlgorithm();
  var tournamentSetup = TournamentSetup.of(players, powers, hasPowerDifficulty);
  long start = System.nanoTime();
  var tournament =
      algorithm.computeBestTournament(tournamentSetup, new ShuffledOrderedDistributionGenerator());
  long duration = System.nanoTime() - start;
  System.out.println(tournament);
  System.out.println("computeBestTournament took " + (duration / 1_000_000.0) + " ms");
}
