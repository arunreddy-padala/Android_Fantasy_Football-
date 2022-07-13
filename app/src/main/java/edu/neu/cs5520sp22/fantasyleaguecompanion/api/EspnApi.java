package edu.neu.cs5520sp22.fantasyleaguecompanion.api;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EspnApi extends AppCompatActivity {

    private static final String teamsUrl = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/teams";
    private static final String athleteUrl = "https://site.web.api.espn.com/apis/common/v3/sports/football/nfl/athletes";

    public static JSONArray getTeams(){
        try {
            JSONObject temp = getJSONFromThread(teamsUrl);
            return temp.getJSONArray("sports").getJSONObject(0).getJSONArray("leagues").getJSONObject(0).getJSONArray("teams");
        } catch (InterruptedException | JSONException exception) {
            exception.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONObject getTeam(String id){
        try {
            return getJSONFromThread(teamsUrl + "/" + id).getJSONObject("team");
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject getAthlete(String id){
        try {
            return getJSONFromThread(athleteUrl + "/" + id).getJSONObject("athlete");
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject getAthleteStats(String id){
        try {
            return getJSONFromThread(athleteUrl + "/" + id + "/stats");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private static JSONObject getJSONFromThread(String url) throws InterruptedException {
        espnThread thread =
                new espnThread(url);
        Thread t = new Thread(thread);
        t.start();
        t.join();
        return thread.getResults();
    }

    static class espnThread implements Runnable{

        private final String stringUrl;
        private JSONObject results;

        public espnThread(String stringUrl){
            this.stringUrl = stringUrl;
        }

        private JSONObject getJSON(){
            JSONObject jObject = new JSONObject();
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
                jObject = new JSONObject(resp.toString());
                httpConnection.getInputStream().close();
                return jObject;

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return jObject;
        }

        @Override
        public void run() {
            results = getJSON();
        }

        public JSONObject getResults(){
            return results;
        }
    }
}