package edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MatchupRepository {
    private MatchupDao matchupDao;
    private MatchupRoomDatabase db;


    public MatchupRepository(Application application) {
        this.db = MatchupRoomDatabase.getDatabase(application);
        this.matchupDao = db.matchupDao();
    }

    public void insertMatchup(Matchup matchup){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit( () -> matchupDao.addMatchup(matchup));
        executorService.shutdown();
    }

    public void deleteMatchup(int matchup_id){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> matchupDao.deleteMatchup(matchup_id));
        executorService.shutdown();
    }
}
