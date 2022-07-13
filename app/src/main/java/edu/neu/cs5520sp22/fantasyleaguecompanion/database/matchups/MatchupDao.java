package edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MatchupDao {

    @Insert
    void addMatchup(Matchup matchup);

    @Insert
    void insertMatchups(Matchup... matchup);

    @Query("DELETE FROM matchups WHERE matchup_id = :matchup_id")
    void deleteMatchup(int matchup_id);

    @Delete
    void deleteMatchups(Matchup... matchup);

    @Query("SELECT * FROM matchups WHERE leagueId = :league_id AND week = :week AND roster_id = :roster_id")
    Matchup findMatchup(String league_id, int week, int roster_id);

    @Query("SELECT * FROM matchups WHERE leagueId = :league_id AND week = :week")
    List<Matchup> findWeeklyMatchups(String league_id, int week);
}
