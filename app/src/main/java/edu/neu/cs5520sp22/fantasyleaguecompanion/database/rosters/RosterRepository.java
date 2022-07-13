package edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RosterRepository {
    private RostersDao rostersDao;
    private RosterRoomDatabase db;

    public RosterRepository(Application application) {
        this.db = RosterRoomDatabase.getDatabase(application);
        this.rostersDao = db.rostersDao();
    }

    public  void insertRoster(Rosters roster){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit( () -> rostersDao.addRoster(roster));
        executorService.shutdown();
    }

    public void deleteRoster(String league_id, int roster_id){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> rostersDao.deleteRoster(league_id, roster_id));
        executorService.shutdown();
    }

    public Rosters findRosterById(String league_id, int roster_id){
        return rostersDao.findRosterById(league_id, roster_id);
    }

    public List<Rosters> findRostersforLeague(String league_id){
        return rostersDao.findRostersforLeague(league_id);
    }

    public Rosters findRosterByUser(String league_id, String owner_id){
        return rostersDao.findRosterByUser(league_id, owner_id);
    }

}
