package edu.neu.cs5520sp22.fantasyleaguecompanion.standings;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueUser;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters.Rosters;
import edu.neu.cs5520sp22.fantasyleaguecompanion.league.LeagueItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ImageStore;

public class StandingItem {
    private final Bitmap image;
    private final String name;
    private final String wins;
    private final String losses;
    private final String ties;
    private final String ppts;
    private final String pts;
    private final String pts_against;

    public StandingItem(Bitmap image, String name, String wins, String losses, String ties, String ppts,
                        String pts, String pts_against) {
        this.image = image;
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.ties = ties;
        this.ppts = ppts;
        this.pts = pts;
        this.pts_against = pts_against;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getWins() {
        return wins;
    }

    public String getLosses() {
        return losses;
    }

    public String getTies() {
        return ties;
    }

    public String getPpts() {
        return ppts;
    }

    public String getPts() {
        return pts;
    }

    public String getPts_against() {
        return pts_against;
    }

    public static StandingItem Builder(Context context, JSONObject jsonObject, LeagueUser user) throws JSONException {
        Bitmap image;
        if(!TextUtils.isEmpty(user.getAvatarUrl())){
            image = ImageStore.loadImage(context, user.getAvatarUrl());
        }
        else{
            image = ImageStore.loadUserImage(context, user.getUserAvatar());
        }
        JSONObject settings = jsonObject.getJSONObject("settings");
        String ppts = "N/A";
        String pts = "N/A";
        String pts_against = "N/A";
        if (settings.has("ppts")){
            ppts = settings.getString("ppts");
            if (settings.has("ppts_decimal")){
                ppts = ppts + "." + settings.getString("ppts_decimal");
            }
        }
        if (settings.has("fpts_against")){
            pts_against = settings.getString("fpts_against");
            if (settings.has("fpts_against_decimal")){
                pts_against = pts_against + "." + settings.getString("fpts_against_decimal");
            }
        }
        if (settings.has("fpts")){
            pts = settings.getString("fpts");
            if (settings.has("fpts_decimal")){
                pts = pts + "." + settings.getString("fpts_decimal");
            }
        }
        return new StandingItem(image, user.getDisplayName(), String.valueOf(settings.getInt("wins")),
                String.valueOf(settings.getInt("losses")), String.valueOf(settings.getInt("ties")), ppts, pts, pts_against);
    }

    public static StandingItem Builder(Context context, Rosters roster, LeagueUser user){
        Bitmap image;
        if(!TextUtils.isEmpty(user.getAvatarUrl())){
            image = ImageStore.loadImage(context, user.getAvatarUrl());
        }
        else{
            image = ImageStore.loadUserImage(context, user.getUserAvatar());
        }
        return new StandingItem(image, user.getDisplayName(), String.valueOf(roster.getWins()),
                String.valueOf(roster.getLosses()), String.valueOf(roster.getTies()),
                String.valueOf(roster.getPpts()), String.valueOf(roster.getFpts()),
                String.valueOf(roster.getFpts_against()));
    }
}
