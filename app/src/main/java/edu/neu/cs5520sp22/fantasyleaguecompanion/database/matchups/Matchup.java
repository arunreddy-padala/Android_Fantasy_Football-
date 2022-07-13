package edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

@Entity(tableName = "matchups", primaryKeys = {"leagueId", "week", "roster_id"})
public class Matchup {
    private @NonNull String leagueId;
    private int week;
    private int roster_id;
    private double points;
    private int matchup_id;
    private ArrayList<String> starters;
    private HashMap<String, Integer> player_points;

    public Matchup(@NonNull String leagueId, int week, int roster_id, double points, int matchup_id, ArrayList<String> starters, HashMap<String, Integer> player_points) {
        this.leagueId = leagueId;
        this.week = week;
        this.roster_id = roster_id;
        this.points = points;
        this.matchup_id = matchup_id;
        this.starters = starters;
        this.player_points = player_points;
    }

    @NonNull
    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(@NonNull String leagueId) {
        this.leagueId = leagueId;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getRoster_id() {
        return roster_id;
    }

    public void setRoster_id(int roster_id) {
        this.roster_id = roster_id;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getMatchup_id() {
        return matchup_id;
    }

    public void setMatchup_id(int matchup_id) {
        this.matchup_id = matchup_id;
    }

    public ArrayList<String> getStarters() {
        return starters;
    }

    public void setStarters(ArrayList<String> starters) {
        this.starters = starters;
    }

    public HashMap<String, Integer> getPlayer_points() {
        return player_points;
    }

    public void setPlayer_points(HashMap<String, Integer> player_points) {
        this.player_points = player_points;
    }

    public static Matchup buildMatchup(JSONObject matchup, String leagueId, int week) throws JSONException {

        return new Matchup(leagueId, week, matchup.getInt("roster_id"),
                matchup.getDouble("points"),matchup.getInt("matchup_id"),
                Utilities.jsonArrayToStringArray(matchup.getJSONArray("starters")),
                Utilities.jsonObjectToHashMap(matchup.getJSONObject("players_points")));
    }
}
