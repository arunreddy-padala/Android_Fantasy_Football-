package edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.neu.cs5520sp22.fantasyleaguecompanion.database.converters.Converters;


@Database(entities = {Matchup.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MatchupRoomDatabase extends RoomDatabase {
    public abstract MatchupDao matchupDao();

    private static MatchupRoomDatabase INSTANCE;

    static  MatchupRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (MatchupRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MatchupRoomDatabase.class,"matchup_database").build();
                }
            }

        }
        return INSTANCE;
    }
}
