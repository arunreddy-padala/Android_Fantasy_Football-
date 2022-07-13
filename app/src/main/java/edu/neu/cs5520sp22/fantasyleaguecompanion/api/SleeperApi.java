package edu.neu.cs5520sp22.fantasyleaguecompanion.api;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;

public class SleeperApi extends AppCompatActivity {

    private static final String baseUrl = "https://api.sleeper.app/v1";

    public static JSONObject getNflState(){
        try {
            return new JSONObject(getJSONFromThread(baseUrl + "/state/nfl"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject getUser(String username){
        try {
            return new JSONObject(getJSONFromThread(baseUrl + "/user/" + username));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONArray getLeagues(String userId, NFLState nflState){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/user/" + userId + "/leagues/nfl/" + nflState.getLeague_season()));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONObject getLeague(String leagueId){
        try {
            return new JSONObject(getJSONFromThread(baseUrl + "/league/" + leagueId));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONArray getLeagueRosters(String leagueId){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/league/" + leagueId + "/rosters"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getLeagueUsers(String leagueId){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/league/" + leagueId + "/users"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getLeagueMatchups(String leagueId, String week){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/league/" + leagueId + "/matchups/" + week));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getLeagueWinnersBracket(String leagueId){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/league/" + leagueId + "/winners_bracket"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getLeagueLosersBracket(String leagueId){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/league/" + leagueId + "/losers_bracket"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getLeagueWeeklyTransactions(String leagueId, String week){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/league/" + leagueId + "/transactions/" + week));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getLeagueTradedPicks(String leagueId){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/league/" + leagueId + "/traded_picks"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getUserDrafts(String userId, NFLState nflState){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/user/" + userId + "/drafts/nfl/" + nflState.getLeague_season()));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getLeagueDrafts(String leagueId){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/league/" + leagueId + "/drafts"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONObject getDraft(String draftId){
        try {
            return new JSONObject(getJSONFromThread(baseUrl + "/draft/" + draftId));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONArray getDraftPicks(String draftId){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/draft/" + draftId + "/picks"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getTradedDraftPicks(String draftId){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/draft/" + draftId + "/traded_picks"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONObject getPlayers(){
        try {
            return new JSONObject(getJSONFromThread(baseUrl + "/players/nfl"));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONArray getTrendingPlayers(String type){
        try {
            return new JSONArray(getJSONFromThread(baseUrl + "/players/nfl/trending/" + type));
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    private static String getJSONFromThread(String url) throws InterruptedException {
        sleeperApiThread thread =
                new sleeperApiThread(url);
        Thread t = new Thread(thread);
        t.start();
        t.join();
        return thread.getResults();
    }

    static class sleeperApiThread implements Runnable{

        private final String stringUrl;
        private String results;

        public sleeperApiThread(String stringUrl){
            this.stringUrl = stringUrl;
        }

        private String getJSON(){
            try{
                URL url = new URL(stringUrl);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setDoInput(true);
                httpConnection.connect();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder resp = new StringBuilder();
                for (String line; (line = reader.readLine()) != null; ) {
                    resp.append(line).append('\n');
                }
                httpConnection.getInputStream().close();
                return resp.toString();

            }
            catch (Exception e){
                e.printStackTrace();
            }

        return "";
        }

        @Override
        public void run() {
            results = getJSON();
        }

        public String getResults(){
            return results;
        }
    }
}