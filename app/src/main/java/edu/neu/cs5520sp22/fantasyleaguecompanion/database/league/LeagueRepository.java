package edu.neu.cs5520sp22.fantasyleaguecompanion.database.league;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeagueRepository {
    private LeagueDao leagueDao;
    private LeagueRoomDatabase db;

    public LeagueRepository(Application application){
        db = LeagueRoomDatabase.getDatabase(application);
        leagueDao = db.leagueDao();
    }

    public void insertLeague(LeagueDbItem league){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> leagueDao.addLeague(league));
        executorService.shutdown();
    }

    public void deleteLeague(String leagueId){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> leagueDao.deleteLeague(leagueId));
        executorService.shutdown();
    }

    public LeagueDbItem findLeagueById(String leagueId){
        return leagueDao.findLeagueById(leagueId);
    }

    public List<LeagueUser> findLeagueUsers(String leagueId){
        return leagueDao.findLeagueUsers(leagueId);
    }

    public void insertLeagueUser(LeagueUser user){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> leagueDao.insertLeagueUser(user));
        executorService.shutdown();
    }

    public LeagueUser findLeagueUserById(String leagueId, String userId){
        return leagueDao.findLeagueUserById(leagueId, userId);
    }

    public LeagueUser findLeagueUserByRosterId(String leagueId, int rosterId){
        return leagueDao.findLeagueUserByRosterId(leagueId, rosterId);
    }
}
