package edu.neu.cs5520sp22.fantasyleaguecompanion.database.player;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerRepository {
    private PlayerDao playerDao;
    private PlayerRoomDatabase db;

    public PlayerRepository(Application application){
        db = PlayerRoomDatabase.getDatabase(application);
        playerDao = db.playerDao();
    }

    private final MutableLiveData<List<Player>> searchResults = new MutableLiveData<>();
    private List<Player> results;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            searchResults.setValue(results);
        }
    };

    public void insertPlayer(Player player){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> playerDao.addPlayer(player));
        executorService.shutdown();
    }

    public void deletePlayer(String full_name){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> playerDao.deletePlayer(full_name));
        executorService.shutdown();
    }

    public void findPlayerByName(String full_name){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            playerDao.findPlayerByName(full_name);
            handler.sendEmptyMessage(0);
        });
        executorService.shutdown();
    }

    public Player findPlayerById(String sleeperId){
        return playerDao.findPlayerById(sleeperId);
    }
}
