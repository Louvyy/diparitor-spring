package com.mebae.diparitor.algorithm.genetic.initializer;

import static com.mebae.diparitor.utils.RandomUtils.newShuffledList;

import com.mebae.diparitor.algorithm.genetic.model.GeneticGame;
import com.mebae.diparitor.algorithm.genetic.model.GeneticTournament;
import com.mebae.diparitor.model.RegisteredPlayer;
import com.mebae.diparitor.model.TournamentSetup;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Generates an initial {@link GeneticTournament} by distributing players to games in order of
 * descending participation count, then shuffling each game's players.
 *
 * <p>
 * This approach attempts to balance player assignments while adding randomness within each game to
 * reduce potential local minima in the genetic algorithm.
 *
 * <p>
 * Note: Because of the deterministic ordered distribution, this method may consistently produce
 * similar initial configurations, increasing the risk of the algorithm getting trapped in the same
 * local optima on repeated runs.
 */
public class ShuffledOrderedDistributionGenerator implements InitialTournamentGenerator {
  /**
   * Generates a viable tournament setup by cyclically assigning players across games sorted by
   * participation count, then shuffling each game's player list.
   *
   * @param tournamentSetup the tournament setup including players and powers
   * @return a {@link GeneticTournament} instance with players distributed and shuffled
   */
  @Override
  public GeneticTournament generate(TournamentSetup tournamentSetup) {

    var gameCount = tournamentSetup.getGameCount();
    var games = Stream.generate(() -> new ArrayList<RegisteredPlayer>()).limit(gameCount).toList();
    var powers = tournamentSetup.getPowers();
    var currentIndex = new AtomicInteger();
    tournamentSetup.getPlayers()
        .stream()
        .sorted(Comparator.comparingInt(RegisteredPlayer::participationCount).reversed())
        .forEach(player -> {
          for (int __ = 0; __ < player.participationCount(); __++) {
            games.get(currentIndex.get()).addLast(player);
            currentIndex.set((currentIndex.get() + 1) % gameCount);
          }
        });

    var gameList =
        games.stream().map(game -> new GeneticGame(newShuffledList(game), powers)).toList();

    return new GeneticTournament(gameList, powers.size(), tournamentSetup.hasPowerDifficulty());
  }
}
