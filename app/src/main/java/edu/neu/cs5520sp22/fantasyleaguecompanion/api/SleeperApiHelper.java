package edu.neu.cs5520sp22.fantasyleaguecompanion.api;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueDbItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueUser;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.Player;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.PlayerRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters.RosterRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters.Rosters;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

public class SleeperApiHelper {

    //Returns ArrayList of 4 ArrayLists {Starters, Bench, Reserve, Taxi}
    public static HashMap<String, ArrayList<String>> getRosterParts(JSONObject rosterObject) throws JSONException {
        HashMap<String, ArrayList<String>> roster = new HashMap<>();
        ArrayList<String> starters = new ArrayList<>();
        ArrayList<String> bench = new ArrayList<>();
        ArrayList<String> reserve = new ArrayList<>();
        ArrayList<String> taxi = new ArrayList<>();
        if(!rosterObject.isNull("taxi")) {
            taxi = Utilities.convertJsonArrayToList(rosterObject.getJSONArray("taxi"));
        }
        if(!rosterObject.isNull("starters")) {
            starters = Utilities.convertJsonArrayToList(rosterObject.getJSONArray("starters"));
        }
        if(!rosterObject.isNull("reserve")) {
            reserve = Utilities.convertJsonArrayToList(rosterObject.getJSONArray("reserve"));
        }
        if(!rosterObject.isNull("players")) {
            bench = Utilities.convertJsonArrayToList(rosterObject.getJSONArray("players"));
        }
        bench.removeIf(starters::contains);
        bench.removeIf(reserve::contains);
        bench.removeIf(taxi::contains);
        roster.put("starters", starters);
        roster.put("bench", bench);
        roster.put("reserve", reserve);
        roster.put("taxi", taxi);
        return roster;
    }

    public static JSONObject getRosterFromLeague(JSONArray leagueRosters, String ownerId) throws JSONException {
        for (int i = 0; i < leagueRosters.length(); i++) {
            if (leagueRosters.getJSONObject(i).getString("owner_id").equals(ownerId)){
                return leagueRosters.getJSONObject(i);
            }
        }
        throw new JSONException("Owner not in league");
    }

    public static ArrayList<JSONObject> getAllTransactions(String leagueId) throws InterruptedException {
        transactionThread thread =
                new transactionThread(leagueId);
        Thread t = new Thread(thread);
        t.start();
        t.join();
        return thread.getTransactions();
    }

    private static class transactionThread implements Runnable{

        private String leagueId;
        private ArrayList<JSONObject> transactions;

        public transactionThread(String leagueId){
            this.leagueId = leagueId;
        }

        private void getAllTransactions() throws JSONException {
            transactions = new ArrayList<>();
            while (true){
                Log.d("league id", leagueId);
                for (int i = 1; i < 18; i++) {
                    transactions.addAll(Utilities.convertJsonArrayToList(SleeperApi.getLeagueWeeklyTransactions(leagueId, Integer.toString(i))));
                }
                if(SleeperApi.getLeague(leagueId).isNull("previous_league_id")){
                    break;
                }
                leagueId = SleeperApi.getLeague(leagueId).getString("previous_league_id");
            }
            Log.d("transactions length", Integer.toString(transactions.size()));
        }

        @Override
        public void run() {
            try {
                getAllTransactions();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public ArrayList<JSONObject> getTransactions(){
            return transactions;
        }
    }

    public static LeagueDbItem getLeague(LeagueRepository db, String leagueId) throws InterruptedException {
        leagueThread thread = new leagueThread(db, leagueId);
        Thread t = new Thread(thread);
        t.start();
        t.join();
        return thread.getLeague();
    }

    private static class leagueThread implements Runnable{

        private String leagueId;
        private LeagueRepository db;
        private LeagueDbItem league;

        leagueThread(LeagueRepository db, String leagueId){
            this.leagueId = leagueId;
            this.db = db;
        }

        @Override
        public void run() {
            league = buildLeague(leagueId, db);
        }

        public LeagueDbItem getLeague(){
            return league;
        }
    }

    private static LeagueDbItem buildLeague(String leagueId, LeagueRepository db){
        LeagueDbItem temp = db.findLeagueById(leagueId);
        if (temp == null){
            JSONObject leagueJson = SleeperApi.getLeague(leagueId);
            try {
                temp = LeagueDbItem.buildLeague(leagueJson);
                db.insertLeague(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public static List<LeagueUser> getLeagueUsers(LeagueRepository db, String leagueId) throws InterruptedException {
        leagueUserThread thread = new leagueUserThread(db, leagueId);
        Thread t = new Thread(thread);
        t.start();
        t.join();
        return thread.getUsers();
    }

    private static class leagueUserThread implements Runnable{

        private String leagueId;
        private LeagueRepository db;
        private List<LeagueUser> users;

        leagueUserThread(LeagueRepository db, String leagueId){
            this.leagueId = leagueId;
            this.db = db;
        }

        @Override
        public void run() {
            LeagueDbItem league = buildLeague(leagueId, db);
            users = getLeagueUsers(league, leagueId, db);
        }

        public List<LeagueUser> getUsers(){
            return users;
        }
    }

    private static List<LeagueUser> getLeagueUsers(LeagueDbItem league, String leagueId, LeagueRepository db){
        int teams = league.getTotal_rosters();
        List<LeagueUser> leagueUsers = db.findLeagueUsers(leagueId);
        if (leagueUsers.size() != teams){
            JSONArray users = SleeperApi.getLeagueUsers(leagueId);
            leagueUsers = new ArrayList<>();
            for (int i = 0; i < users.length(); i++) {
                try {
                    String avatarUrl = null;
                    JSONObject user = users.getJSONObject(i);
                    if(user.getJSONObject("metadata").has("avatar")){
                        avatarUrl = user.getJSONObject("metadata").getString("avatar");
                    }
                    LeagueUser dbUser = new LeagueUser(leagueId, user.getString("user_id"),
                            user.getString("display_name"),
                            avatarUrl,
                            user.getString("avatar"),
                            SleeperApiHelper.getRosterId(leagueId, user.getString("user_id")));
                    db.insertLeagueUser(dbUser);
                    leagueUsers.add(dbUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return leagueUsers;
    }

    public static int getRosterId(String leagueId, String userId){
        JSONArray rosters = SleeperApi.getLeagueRosters(leagueId);
        for (int i = 0; i < rosters.length(); i++) {
            try {
                JSONObject roster = rosters.getJSONObject(i);
                if(roster.getString("owner_id").equals(userId)){
                    return roster.getInt("roster_id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static boolean reloadPlayers(PlayerRepository playerRepository, SharedPreferences p){
        return getPlayers(playerRepository, p, true);
    }

    public static boolean getPlayers(PlayerRepository playerRepository, SharedPreferences p){
        return getPlayers(playerRepository, p, false);
    }

    private static boolean getPlayers(PlayerRepository playerRepository, SharedPreferences p, boolean reload){
        if(reload){
            updatePlayers(playerRepository, p);
        }
        else {
            Instant now = Instant.now();
            now.minus(Integer.parseInt(p.getString("reload_players_interval", "48")), ChronoUnit.HOURS);
            Log.d("shared", "check");
            long last_check = p.getLong("player_datetime", 0);
            Log.d("shared", String.valueOf(last_check));
            Instant last = Instant.ofEpochMilli(last_check);
            if (now.compareTo(last) <= 0 || last_check == 0) {
                Log.d("time", "time");
                updatePlayers(playerRepository, p);
            } else {
                Log.d("time", "up to date");
            }
        }
        return true;
    }

    private static void updatePlayers(PlayerRepository playerRepository, SharedPreferences p){
        JSONObject players = SleeperApi.getPlayers();
        for (Iterator<String> it = players.keys(); it.hasNext(); ) {
            String id = it.next();
            try {
                JSONObject player = players.getJSONObject(id);
                if(!TextUtils.isEmpty(player.getString("player_id"))){
                    playerRepository.insertPlayer(Player.PlayerBuilder(player));
                }
            } catch (JSONException e) {
                Log.d("id error", id);
                e.printStackTrace();
            }
        }
        Log.d("players length", String.valueOf(players.length()));
        Log.d("shared", "added");
        SharedPreferences.Editor player_datetime = p.edit();
        player_datetime.putLong("player_datetime", Instant.now().toEpochMilli());
        player_datetime.apply();
    }

    public static void saveRosters(RosterRepository rosterRepository, LeagueDbItem leagueDbItem) {
        List<Rosters> rosters = rosterRepository.findRostersforLeague(leagueDbItem.getLeagueId());
        if (rosters.size() != leagueDbItem.getTotal_rosters()){
            JSONArray jsonRosters = SleeperApi.getLeagueRosters(leagueDbItem.getLeagueId());
            for (int i = 0; i < jsonRosters.length(); i++) {
                try {
                    Rosters roster = Rosters.RosterBuilder(jsonRosters.getJSONObject(i));
                    rosterRepository.insertRoster(roster);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getUserId(String username) throws InterruptedException {
        userThread thread = new userThread(username);
        Thread t = new Thread(thread);
        t.start();
        t.join();
        return thread.getUserId();
    }

    static class userThread implements Runnable{

        String username;
        String userId;

        public userThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            JSONObject json = SleeperApi.getUser(username);
            try {
                userId = json.getString("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getUserId() {
            return userId;
        }
    }
}
