package com.mebae.diparitor.algorithm;

import com.mebae.diparitor.model.PairingResult;
import com.mebae.diparitor.model.TournamentSetup;

@FunctionalInterface
public interface Algorithm {
  PairingResult computeBestTournament(TournamentSetup tournamentSetup);
}
