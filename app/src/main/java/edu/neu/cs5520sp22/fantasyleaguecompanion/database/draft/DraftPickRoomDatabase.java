package edu.neu.cs5520sp22.fantasyleaguecompanion.database.draft;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.neu.cs5520sp22.fantasyleaguecompanion.database.converters.Converters;

@Database(entities = {DraftDbItem.class, DraftPick.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class DraftPickRoomDatabase extends RoomDatabase {
    public abstract DraftPickDao draftPickDao();

    private static DraftPickRoomDatabase INSTANCE;

    static DraftPickRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (DraftPickRoomDatabase.class) {
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DraftPickRoomDatabase.class, "draft_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
