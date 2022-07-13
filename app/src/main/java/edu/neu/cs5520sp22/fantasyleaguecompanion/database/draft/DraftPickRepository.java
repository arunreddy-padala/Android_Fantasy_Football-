package edu.neu.cs5520sp22.fantasyleaguecompanion.database.draft;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DraftPickRepository {
    private DraftPickDao draftPickDao;
    private DraftPickRoomDatabase db;

    public DraftPickRepository(Application application){
        db = DraftPickRoomDatabase.getDatabase(application);
        draftPickDao = db.draftPickDao();
    }

    private final MutableLiveData<List<DraftPick>> searchResults = new MutableLiveData<>();
    private List<DraftPick> results;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            searchResults.setValue(results);
        }
    };

    public void insertDraftPick(DraftPick pick){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> draftPickDao.addDraftPick(pick));
        executorService.shutdown();
    }

    public boolean draftExists(String draftId){
        return draftPickDao.draftExists(draftId);
    }

    public void deleteDraftPick(String id){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //executorService.submit(() -> draftPickDao.deleteDraftPick(id));
        executorService.shutdown();
    }

    public DraftPick findDraftPick(String draftId, int round, int pick){
        return draftPickDao.findDraftPick(draftId, round, pick);
    }
}
