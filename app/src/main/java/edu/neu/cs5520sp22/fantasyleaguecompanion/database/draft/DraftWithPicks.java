package edu.neu.cs5520sp22.fantasyleaguecompanion.database.draft;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DraftWithPicks {
    @Embedded public DraftDbItem draft;
    @Relation(
            parentColumn = "draftId",
            entityColumn = "draftId"
    )
    public List<DraftPick> draftPicks;
}
