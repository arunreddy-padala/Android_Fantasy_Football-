package edu.neu.cs5520sp22.fantasyleaguecompanion.database.player;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class}, version = 1)
public abstract class PlayerRoomDatabase extends RoomDatabase{

    public abstract PlayerDao playerDao();

    private static PlayerRoomDatabase INSTANCE;

    static PlayerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlayerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlayerRoomDatabase.class, "player_database")
                            //.fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
