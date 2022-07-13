package edu.neu.cs5520sp22.fantasyleaguecompanion.roster;

import android.graphics.Bitmap;

public class PlayerRow {
    private final String displayName;
    private final String rosterSpot;
    private final Bitmap image;
    private final String position;
    private final String team;

    public PlayerRow(String displayName, String rosterSpot, Bitmap image, String position, String team) {
        this.displayName = displayName;
        this.rosterSpot = rosterSpot;
        this.image = image;
        this.position = position;
        this.team = team;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRosterSpot() {
        return rosterSpot;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return team;
    }

    public static PlayerRow EmptySpot(String rosterSpot){
        return new PlayerRow("Empty", rosterSpot, null, "", "");
    }
}
