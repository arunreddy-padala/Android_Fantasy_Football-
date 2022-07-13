package edu.neu.cs5520sp22.fantasyleaguecompanion.utils;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApi;

public class NFLState implements Parcelable {
    private int week;

    protected NFLState(Parcel in) {
        week = in.readInt();
        season = in.readString();
        leg = in.readInt();
        league_season = in.readString();
        display_week = in.readInt();
    }

    public static final Creator<NFLState> CREATOR = new Creator<NFLState>() {
        @Override
        public NFLState createFromParcel(Parcel in) {
            return new NFLState(in);
        }

        @Override
        public NFLState[] newArray(int size) {
            return new NFLState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(week);
        parcel.writeString(season);
        parcel.writeInt(leg);
        parcel.writeString(league_season);
        parcel.writeInt(display_week);
    }

    public enum Season_Type {
        OFF, PRE, REGULAR, POST, NULL;
        public static Season_Type getType(String season_type){
            switch (season_type) {
                case "off":
                    return Season_Type.OFF;
                case "pre":
                    return Season_Type.PRE;
                case "regular":
                    return Season_Type.REGULAR;
                case "post":
                    return Season_Type.POST;
                default:
                    return Season_Type.NULL;
            }
        }
    }
    private Season_Type seasonType;
    private String season;
    private int leg;
    private String league_season;
    private int display_week;

    public NFLState(int week, Season_Type seasonType, String season, int leg, String league_season, int display_week) {
        this.week = week;
        this.seasonType = seasonType;
        this.season = season;
        this.leg = leg;
        this.league_season = league_season;
        this.display_week = display_week;
    }

    public static NFLState buildNFLState(JSONObject json) throws JSONException {
        return new NFLState(json.getInt("week"),
                Season_Type.getType(json.getString("season_type")),
                json.getString("season"), json.getInt("leg"),
                json.getString("league_season"), json.getInt("display_week"));
    }

    public int getWeek() {
        return week;
    }

    public Season_Type getSeasonType() {
        return seasonType;
    }

    public String getSeason() {
        return season;
    }

    public int getLeg() {
        return leg;
    }

    public String getLeague_season() {
        return league_season;
    }

    public int getDisplay_week() {
        return display_week;
    }

    public static class nflStateThread implements Runnable{
        private NFLState nflState;

        @Override
        public void run() {
            try {
                nflState = buildNFLState(SleeperApi.getNflState());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public NFLState getNflState() {
            return nflState;
        }
    }
}
