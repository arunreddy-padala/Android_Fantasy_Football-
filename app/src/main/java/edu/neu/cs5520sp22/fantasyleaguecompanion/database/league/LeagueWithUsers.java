package edu.neu.cs5520sp22.fantasyleaguecompanion.database.league;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class LeagueWithUsers {
    @Embedded public LeagueDbItem league;
    @Relation(
            parentColumn = "leagueId",
            entityColumn = "leagueId"
    )
    public List<LeagueUser> leagueUsers;
}
