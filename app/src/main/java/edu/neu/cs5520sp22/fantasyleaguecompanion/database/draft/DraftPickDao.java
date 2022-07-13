package edu.neu.cs5520sp22.fantasyleaguecompanion.database.draft;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DraftPickDao {
    @Query("SELECT * FROM draftPicks")
    LiveData<List<DraftPick>> getAllDraftPicks();

    @Query("SELECT * FROM draftPicks WHERE draftId = :draftId")
    List<DraftPick> findDrafts(String draftId);

    @Query("SELECT * FROM draftPicks WHERE draftId = :draftId AND round = :round AND pick = :pick")
    DraftPick findDraftPick(String draftId, int round, int pick);

    @Query("SELECT EXISTS (SELECT 1 FROM draftPicks WHERE draftId = :draftId)")
    boolean draftExists(String draftId);

    @Insert
    void addDraftPick(DraftPick DraftPick);

    @Insert
    void insertDraftPicks(DraftPick... DraftPicks);

    @Delete
    void deleteDraftPicks(DraftPick... draftPicks);

    @Update
    void updateDraftPicks(DraftPick... draftPicks);
}
