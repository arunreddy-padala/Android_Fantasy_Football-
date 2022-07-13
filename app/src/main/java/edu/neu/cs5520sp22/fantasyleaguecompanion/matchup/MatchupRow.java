package edu.neu.cs5520sp22.fantasyleaguecompanion.matchup;

import android.graphics.Bitmap;

public class MatchupRow {
    private final Bitmap userimage1;
    private final Bitmap userimage2;
//    private final String username1;
//    private final String username2;
    private final String teamname1;
    private final String teamname2;
    private final String points1;
    private final String points2;

    public MatchupRow(Bitmap userimage1, Bitmap userimage2, String teamname1, String teamname2, double points1, double points2) {
        this.userimage1 = userimage1;
        this.userimage2 = userimage2;
        this.teamname1 = teamname1;
        this.teamname2 = teamname2;
        this.points1 = String.valueOf(points1);
        this.points2 = String.valueOf(points2);
    }

//    public MatchupRow(Bitmap userimage1, Bitmap userimage2, String username1, String username2,
//                      String teamname1, String teamname2, double points1, double points2) {
//        this.userimage1 = userimage1;
//        this.userimage2 = userimage2;
//        this.username1 = username1;
//        this.username2 = username2;
//        this.teamname1 = teamname1;
//        this.teamname2 = teamname2;
//        this.points1 = String.valueOf(points1);
//        this.points2 = String.valueOf(points2);
//    }

    public Bitmap getUserimage1() {
        return userimage1;
    }

    public Bitmap getUserimage2() {
        return userimage2;
    }
//
//    public String getUsername1() {
//        return username1;
//    }
//
//    public String getUsername2() {
//        return username2;
//    }

    public String getTeamname1() {
        return teamname1;
    }

    public String getTeamname2() {
        return teamname2;
    }

    public String getPoints1() {
        return points1;
    }

    public String getPoints2() {
        return points2;
    }
}
