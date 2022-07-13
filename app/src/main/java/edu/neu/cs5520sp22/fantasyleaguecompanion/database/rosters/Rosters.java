package edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

@Entity(tableName = "rosters", primaryKeys = {"league_id", "roster_id"})
public class Rosters {
    private ArrayList<String> starters;
    private ArrayList<String> taxi;
    private ArrayList<String> reserve;
    private ArrayList<String> players;
    private ArrayList<String> bench;
    private int wins;
    private int waiver_position;
    private int total_moves;
    private int ties;
    private double ppts;
    private int losses;
    private double fpts;
    private double fpts_against;
    private int waiver_budget_used;
    private int division;
    private int roster_id;
    private String owner_id;
    private @NonNull String league_id;

    public Rosters(ArrayList<String> starters, int wins, int waiver_position, int total_moves,
                   int ties, double ppts, int losses, double fpts, double fpts_against,
                   int division, int roster_id, ArrayList<String> reserve,
                   ArrayList<String> players, String owner_id, @NonNull String league_id,
                   ArrayList<String> taxi, int waiver_budget_used) {
        this.starters = starters;
        this.wins = wins;
        this.waiver_position = waiver_position;
        this.total_moves = total_moves;
        this.ties = ties;
        this.ppts = ppts;
        this.losses = losses;
        this.fpts = fpts;
        this.fpts_against = fpts_against;
        this.division = division;
        this.roster_id = roster_id;
        this.reserve = reserve;
        this.players = players;
        this.owner_id = owner_id;
        this.league_id = league_id;
        this.taxi = taxi;
        this.waiver_budget_used = waiver_budget_used;
        buildBench();
    }

    private void buildBench(){
        this.bench = (ArrayList<String>) this.players.clone();
        this.bench.removeAll(starters);
        if(reserve != null) {
            this.bench.removeAll(reserve);
        }
        if(taxi != null) {
            this.bench.removeAll(taxi);
        }
        Log.d("bench", bench.toString());
    }

    public ArrayList<String> getBench() {
        return bench;
    }

    public void setBench(ArrayList<String> bench) {
        this.bench = bench;
    }

    public ArrayList<String> getStarters() {
        return starters;
    }

    public void setStarters(ArrayList<String> starters) {
        this.starters = starters;
    }

    public int getRoster_id() {
        return roster_id;
    }

    public void setRoster_id(int roster_id) {
        this.roster_id = roster_id;
    }

    public ArrayList<String> getReserve() {
        return reserve;
    }

    public void setReserve(ArrayList<String> reserve) {
        this.reserve = reserve;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    @NonNull
    public String getLeague_id() {
        return league_id;
    }

    public void setLeague_id(@NonNull String league_id) {
        this.league_id = league_id;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getWaiver_position() {
        return waiver_position;
    }

    public void setWaiver_position(int waiver_position) {
        this.waiver_position = waiver_position;
    }

    public int getTotal_moves() {
        return total_moves;
    }

    public void setTotal_moves(int total_moves) {
        this.total_moves = total_moves;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public double getFpts() {
        return fpts;
    }

    public void setFpts(double fpts) {
        this.fpts = fpts;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public double getPpts() {
        return ppts;
    }

    public void setPpts(double ppts) {
        this.ppts = ppts;
    }

    public double getFpts_against() {
        return fpts_against;
    }

    public void setFpts_against(double fpts_against) {
        this.fpts_against = fpts_against;
    }

    public ArrayList<String> getTaxi() {
        return taxi;
    }

    public void setTaxi(ArrayList<String> taxi) {
        this.taxi = taxi;
    }

    public int getWaiver_budget_used() {
        return waiver_budget_used;
    }

    public void setWaiver_budget_used(int waiver_budget_used) {
        this.waiver_budget_used = waiver_budget_used;
    }

    public static Rosters RosterBuilder(JSONObject roster) throws JSONException {
        ArrayList<String> starters = Utilities.jsonArrayToStringArray(roster.getJSONArray("starters"));
        ArrayList<String> reserve = null;
        if(!roster.getString("reserve").equals("null")) {
            reserve = Utilities.jsonArrayToStringArray(roster.getJSONArray("reserve"));
        }
        ArrayList<String> taxi = null;
        if(!roster.getString("taxi").equals("null")) {
            taxi = Utilities.jsonArrayToStringArray(roster.getJSONArray("taxi"));
        }
        ArrayList<String> players = Utilities.jsonArrayToStringArray(roster.getJSONArray("players"));
        String owner_id = roster.getString("owner_id");
        String league_id = roster.getString("league_id");

        JSONObject settingsObject = roster.getJSONObject("settings");
        int wins = settingsObject.getInt("wins");
        int division = settingsObject.getInt("division");
        int waiver_position = settingsObject.getInt("waiver_position");
        int waiver_budget_used = settingsObject.getInt("waiver_budget_used");
        int total_moves = settingsObject.getInt("total_moves");
        int ties = settingsObject.getInt("ties");
        int losses = settingsObject.getInt("losses");
        double fpts = 0;
        int roster_id = roster.getInt("roster_id");

        double fpts_against=0;
        double ppts = 0;

        if(!settingsObject.isNull("fpts_decimal")){
            fpts = Double.parseDouble(settingsObject.getInt("fpts") + "." + settingsObject.getInt("fpts_decimal"));
        }

        if(!settingsObject.isNull("fpts_against")){
            if(!settingsObject.isNull("fpts_against_decimal")){
                fpts_against = Double.parseDouble(settingsObject.getInt("fpts_against") + "." + settingsObject.getInt("fpts_against_decimal"));
            }
            else{
                fpts_against = settingsObject.getInt("fpts_against");
            }
        }

        if(!settingsObject.isNull("ppts")){
            if(!settingsObject.isNull("ppts_decimal")){
                ppts = Double.parseDouble(settingsObject.getInt("ppts") + "." + settingsObject.getInt("ppts_decimal"));
            }
            else{
                ppts = settingsObject.getInt("ppts");
            }
        }

        return new Rosters(starters, wins, waiver_position, total_moves, ties, ppts, losses, fpts,
                fpts_against, division, roster_id,reserve, players, owner_id, league_id, taxi,
                waiver_budget_used);
    }
}
