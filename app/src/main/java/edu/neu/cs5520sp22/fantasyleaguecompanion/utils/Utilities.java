package edu.neu.cs5520sp22.fantasyleaguecompanion.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.neu.cs5520sp22.fantasyleaguecompanion.MainActivity;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueUser;
import edu.neu.cs5520sp22.fantasyleaguecompanion.league.LeagueItem;

public class Utilities {

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> convertJsonArrayToList(JSONArray json){
        ArrayList<T> results = new ArrayList<>();
        if (json != null) {
            for (int i = 0;i < json.length(); i++){
                try {
                    results.add((T) json.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return results;
    }

    public static String rosterView(String position){
        if(position.equals("FLEX")){
            return "WRT";
        }
        if(position.equals("SUPER_FLEX")){
            return "WR\nTQ";
        }
        if(position.equals("WRRB_FLEX")){
            return "WR";
        }
        if(position.equals("REC_FLEX")){
            return "WT";
        }
        if(position.equals("IDP_FLEX")){
            return "IDP";
        }
        return position;
    }

    public static final String[] ROSTER_ORDER = new String[] {"starters", "bench", "reserve", "taxi"};
    public static final String[] FLEX = new String[] {"RB", "WR", "TE"};
    public static final String[] SUPER_FLEX = new String[] {"QB", "RB", "WR", "TE"};
    public static final String[] WRRB_FLEX = new String[] {"RB", "WR"};
    public static final String[] REC_FLEX = new String[] {"WR", "TE"};
    public static final String[] IDP_FLEX = new String[] {"DL", "LB", "DB"};

    public enum RosterColor {
        QB("#ff2a6d"),
        RB("#00ceb8"),
        WR("#58a7ff"),
        TE("#ffae58"),
        K("#bd66ff"),
        DEF("#fff67a"),
        DL("#ff795a"),
        LB("#6d7df5"),
        DB("#ff7cb6"),
        BN("#b6d0eb");

        public final String color;

        RosterColor(String color) {
            this.color = color;
        }
    }

    public static boolean isRosterPosition(String position) {
        for (RosterColor c : RosterColor.values()) {
            if (c.name().equals(position)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> jsonArrayToStringArray(JSONArray json) throws JSONException {
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            results.add(json.getString(i));
        }
        return results;
    }

    public static HashMap<String, Integer> jsonObjectToHashMap(JSONObject json) throws  JSONException {
        HashMap<String, Integer> results = new HashMap<>();
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();
            results.put(key, json.getInt(key));
        }
        return results;
    }

    public static ArrayList<LeagueItem> convertFromDb(List<LeagueUser> dbUsers, Context context){
        ArrayList<LeagueItem> users = new ArrayList<>();
        for (LeagueUser dbUser : dbUsers) {
            Bitmap image;
            if(!TextUtils.isEmpty(dbUser.getAvatarUrl())){
                image = ImageStore.loadImage(context, dbUser.getAvatarUrl());
            }
            else{
                image = ImageStore.loadUserImage(context, dbUser.getUserAvatar());
            }
            users.add(new LeagueItem(dbUser.getUserId(), dbUser.getDisplayName(), image));
        }
        return users;
    }

    public static void logOut(AppCompatActivity context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user_id");
        editor.remove("username");
        editor.apply();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
    }
}
