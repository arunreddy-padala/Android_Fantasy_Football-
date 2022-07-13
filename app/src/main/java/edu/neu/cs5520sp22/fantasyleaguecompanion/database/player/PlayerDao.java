package edu.neu.cs5520sp22.fantasyleaguecompanion.database.player;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM players")
    LiveData<List<Player>> getAllPlayers();

    @Query("SELECT * FROM players WHERE full_name = :name")
    List<Player> findPlayerByName(String name);

    @Query("SELECT * FROM players WHERE sleeperId = :player_id")
    Player findPlayerById(String player_id);

    @Insert
    void addPlayer(Player player);

    @Insert
    void insertPlayers(Player... players);

    @Query("DELETE FROM players WHERE full_name = :name")
    void deletePlayer(String name);

    @Delete
    void deletePlayers(Player... players);

    @Update
    void updatePlayers(Player... players);
}
