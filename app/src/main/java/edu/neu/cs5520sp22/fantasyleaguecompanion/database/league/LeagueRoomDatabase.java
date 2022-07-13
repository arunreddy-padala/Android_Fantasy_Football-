package edu.neu.cs5520sp22.fantasyleaguecompanion.database.league;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.neu.cs5520sp22.fantasyleaguecompanion.database.converters.Converters;

@Database(entities = {LeagueDbItem.class, LeagueUser.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class LeagueRoomDatabase extends RoomDatabase {

    public abstract LeagueDao leagueDao();

    private static LeagueRoomDatabase INSTANCE;

    static LeagueRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LeagueRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LeagueRoomDatabase.class, "league_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
