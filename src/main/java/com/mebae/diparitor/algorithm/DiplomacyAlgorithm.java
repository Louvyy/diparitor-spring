package com.mebae.diparitor.algorithm;

import com.mebae.diparitor.algorithm.genetic.initializer.InitialTournamentGenerator;
import com.mebae.diparitor.model.PairingResult;
import com.mebae.diparitor.model.TournamentSetup;

// TODO JAVADOC
@FunctionalInterface
public interface DiplomacyAlgorithm {
  PairingResult computeBestTournament(TournamentSetup tournamentSetup, InitialTournamentGenerator initialTournamentGenerator);
}
