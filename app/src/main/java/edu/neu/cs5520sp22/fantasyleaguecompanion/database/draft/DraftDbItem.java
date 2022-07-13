package edu.neu.cs5520sp22.fantasyleaguecompanion.database.draft;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;

@Entity(tableName = "drafts")
public class DraftDbItem {
    @PrimaryKey
    private @NonNull String draftId;
    private int rounds;
    private int teams;
    private HashMap<String, Integer> draftOrder;

    public DraftDbItem(@NonNull String draftId, int rounds, int teams, HashMap<String, Integer> draftOrder) {
        this.draftId = draftId;
        this.rounds = rounds;
        this.teams = teams;
        this.draftOrder = draftOrder;
    }

    @NonNull
    public String getDraftId() {
        return draftId;
    }

    public void setDraftId(@NonNull String draftId) {
        this.draftId = draftId;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getTeams() {
        return teams;
    }

    public void setTeams(int teams) {
        this.teams = teams;
    }

    public HashMap<String, Integer> getDraftOrder() {
        return draftOrder;
    }

    public void setDraftOrder(HashMap<String, Integer> draftOrder) {
        this.draftOrder = draftOrder;
    }
}
