package edu.neu.cs5520sp22.fantasyleaguecompanion.database.league;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LeagueDao {
    @Query("SELECT * FROM leagues")
    LiveData<List<LeagueDbItem>> getAllLeagues();

    @Query("SELECT * FROM leagues WHERE leagueId = :leagueId")
    LeagueDbItem findLeagueById(String leagueId);

    @Insert
    void addLeague(LeagueDbItem league);

    @Insert
    void insertLeagues(LeagueDbItem... leagues);

    @Query("DELETE FROM leagues WHERE leagueId = :leagueId")
    void deleteLeague(String leagueId);

    @Delete
    void deleteLeagues(LeagueDbItem... leagues);

    @Update
    void updateLeagues(LeagueDbItem... leagues);

    @Query("SELECT * FROM leagueUsers")
    LiveData<List<LeagueUser>> getAllLeagueUsers();

    @Query("SELECT * FROM leagueUsers WHERE leagueId = :leagueId")
    List<LeagueUser> findLeagueUsers(String leagueId);

    @Query("SELECT * FROM leagueUsers WHERE leagueId = :leagueId AND userId = :userId")
    LeagueUser findLeagueUserById(String leagueId, String userId);

    @Query("SELECT * FROM leagueUsers WHERE leagueId = :leagueId AND roster_id = :rosterId")
    LeagueUser findLeagueUserByRosterId(String leagueId, int rosterId);

    @Insert
    void addLeagueUser(LeagueUser leagueUser);

    @Insert
    void insertLeagueUser(LeagueUser... leagueUsers);

    @Query("DELETE FROM leagueUsers WHERE leagueId = :leagueId AND userId = :userId")
    void deleteLeagueUser(String leagueId, String userId);

    @Delete
    void deleteLeagueUser(LeagueUser... leagueUser);

    @Update
    void updateLeagueUser(LeagueUser... leagueUsers);
}
