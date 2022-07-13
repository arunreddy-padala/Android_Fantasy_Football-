package edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.neu.cs5520sp22.fantasyleaguecompanion.database.converters.Converters;


@Database(entities = {Rosters.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RosterRoomDatabase extends RoomDatabase {
    public abstract  RostersDao rostersDao();

    private static RosterRoomDatabase INSTANCE;

    static RosterRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (RosterRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RosterRoomDatabase.class,"roster_database").build();
                }
            }

        }
        return INSTANCE;
    }
}
