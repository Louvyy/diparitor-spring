package com.mebae.diparitor.algorithm;

import com.mebae.diparitor.data.Power;
import com.mebae.diparitor.data.RegisteredPlayer;
import com.mebae.diparitor.data.TournamentSetup;

import java.util.List;
import java.util.Map;

public interface Algorithm {
  List<Map<Power, RegisteredPlayer>> computeBestTournament(TournamentSetup tournamentSetup);
}
