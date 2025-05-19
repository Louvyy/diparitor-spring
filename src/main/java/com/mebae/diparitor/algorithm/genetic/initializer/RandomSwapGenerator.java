package com.mebae.diparitor.algorithm.genetic.initializer;

import static com.mebae.diparitor.utils.RandomUtils.newShuffledList;

import com.mebae.diparitor.algorithm.genetic.model.GeneticGame;
import com.mebae.diparitor.algorithm.genetic.model.GeneticTournament;
import com.mebae.diparitor.model.Power;
import com.mebae.diparitor.model.RegisteredPlayer;
import com.mebae.diparitor.model.TournamentSetup;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Generates an initial {@link GeneticTournament} by randomly shuffling all participants and
 * assigning them to games, followed by random swaps to ensure tournament viability.
 *
 * <p>
 * This generator prioritizes randomness while still guaranteeing a valid tournament state by
 * correcting potential player duplication or constraint violations using swaps.
 */
public class RandomSwapGenerator implements InitialTournamentGenerator {
  private static List<RegisteredPlayer> generateShuffledParticipantList(
      Set<RegisteredPlayer> players) {
    var participantList = players.stream()
        .flatMap(player -> Stream.generate(() -> player).limit(player.participationCount()))
        .toList();
    return newShuffledList(participantList);
  }

  private static List<GeneticGame> generateRandomGameList(Set<RegisteredPlayer> players,
                                                          List<Power> powers) {
    var participantList = generateShuffledParticipantList(players);
    var geneCount = powers.size();
    return IntStream.range(0, participantList.size() / geneCount)
        .mapToObj(i -> new GeneticGame(participantList.subList(i * geneCount, (i + 1) * geneCount),
            powers))
        .toList();
  }

  /**
   * Generates an initial tournament using a randomized distribution strategy, followed by swaps
   * until the tournament becomes viable.
   *
   * @param tournamentSetup the setup parameters of the tournament
   * @return a viable {@link GeneticTournament} instance
   */
  @Override
  public GeneticTournament generate(TournamentSetup tournamentSetup) {
    var powers = tournamentSetup.getPowers();
    var gameList = generateRandomGameList(tournamentSetup.getPlayers(), powers);
    var tournament =
        new GeneticTournament(gameList, powers.size(), tournamentSetup.hasPowerDifficulty());
    while (!tournament.isViable()) {
      tournament = tournament.computeRandomViablePlayerSwap();
    }
    return tournament;
  }
}
