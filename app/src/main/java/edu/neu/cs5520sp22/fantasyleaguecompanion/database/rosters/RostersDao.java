package edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RostersDao {

    @Query("SELECT * FROM rosters WHERE league_id = :league_id AND roster_id = :roster_id")
    Rosters findRosterById(String league_id, int roster_id);

    @Query("SELECT * FROM rosters WHERE league_id = :league_id AND owner_id = :owner_id")
    Rosters findRosterByUser(String league_id, String owner_id);

    @Query("SELECT * FROM rosters WHERE league_id = :league_id")
    List<Rosters> findRostersforLeague(String league_id);

    @Insert
    void addRoster(Rosters rosters);

    @Insert
    void insertRosters(Rosters ... rosters);

    @Query("DELETE FROM rosters WHERE league_id = :league_id AND roster_id = :roster_id")
    void deleteRoster(String league_id, int roster_id);

    @Delete
    void deleteRosters(Rosters... rosters);
}
