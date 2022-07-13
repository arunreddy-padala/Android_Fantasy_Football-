package edu.neu.cs5520sp22.fantasyleaguecompanion.database.draft;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "draftPicks", primaryKeys = {"draftId","round", "pick"})
public class DraftPick {
    private @NonNull String draftId;
    private int round;
    private int pick;
    private String picked_by;
    private String image;
    private String name;
    private String position;

    public DraftPick(@NonNull String draftId, int round, int pick, String picked_by, String image, String name, String position) {
        this.draftId = draftId;
        this.round = round;
        this.pick = pick;
        this.picked_by = picked_by;
        this.image = image;
        this.name = name;
        this.position = position;
    }

    public static DraftPick ownerBuilder(String draftId, int pick, String image, String name) {
        return new DraftPick(draftId, 0, pick, "", image, name, "");
    }

    @NonNull
    public String getDraftId() {
        return draftId;
    }

    public void setDraftId(@NonNull String draftId) {
        this.draftId = draftId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPick() {
        return pick;
    }

    public void setPick(int pick) {
        this.pick = pick;
    }

    public String getPicked_by() {
        return picked_by;
    }

    public void setPicked_by(String picked_by) {
        this.picked_by = picked_by;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
