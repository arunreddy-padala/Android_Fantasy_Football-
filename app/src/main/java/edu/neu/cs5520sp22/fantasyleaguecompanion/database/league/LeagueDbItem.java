package edu.neu.cs5520sp22.fantasyleaguecompanion.database.league;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

@Entity(tableName = "leagues")
public class LeagueDbItem implements Parcelable {
    @PrimaryKey
    private @NonNull String leagueId;
    private int total_rosters;
    private String status;
    private int divisions;
    private String season_type;
    private String season;
    private ArrayList<String> roster_positions;
    private String previous_league_id;
    private String name;
    private String draft_id;
    private String avatar;

    public LeagueDbItem(@NonNull String leagueId, int total_rosters, String status, int divisions,
                        String season_type, String season, ArrayList<String> roster_positions,
                        String previous_league_id, String name, String draft_id, String avatar) {
        this.leagueId = leagueId;
        this.total_rosters = total_rosters;
        this.status = status;
        this.divisions = divisions;
        this.season_type = season_type;
        this.season = season;
        this.roster_positions = roster_positions;
        this.previous_league_id = previous_league_id;
        this.name = name;
        this.draft_id = draft_id;
        this.avatar = avatar;
    }

    protected LeagueDbItem(Parcel in) {
        leagueId = in.readString();
        total_rosters = in.readInt();
        status = in.readString();
        divisions = in.readInt();
        season_type = in.readString();
        season = in.readString();
        roster_positions = in.createStringArrayList();
        previous_league_id = in.readString();
        name = in.readString();
        draft_id = in.readString();
        avatar = in.readString();
    }

    public static final Creator<LeagueDbItem> CREATOR = new Creator<LeagueDbItem>() {
        @Override
        public LeagueDbItem createFromParcel(Parcel in) {
            return new LeagueDbItem(in);
        }

        @Override
        public LeagueDbItem[] newArray(int size) {
            return new LeagueDbItem[size];
        }
    };

    @NonNull
    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(@NonNull String leagueId) {
        this.leagueId = leagueId;
    }

    public int getTotal_rosters() {
        return total_rosters;
    }

    public void setTotal_rosters(int total_rosters) {
        this.total_rosters = total_rosters;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDivisions() {
        return divisions;
    }

    public void setDivisions(int divisions) {
        this.divisions = divisions;
    }

    public String getSeason_type() {
        return season_type;
    }

    public void setSeason_type(String season_type) {
        this.season_type = season_type;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public ArrayList<String> getRoster_positions() {
        return roster_positions;
    }

    public void setRoster_positions(ArrayList<String> roster_positions) {
        this.roster_positions = roster_positions;
    }

    public String getPrevious_league_id() {
        return previous_league_id;
    }

    public void setPrevious_league_id(String previous_league_id) {
        this.previous_league_id = previous_league_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDraft_id() {
        return draft_id;
    }

    public void setDraft_id(String draft_id) {
        this.draft_id = draft_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static LeagueDbItem buildLeague(JSONObject leagueJson) throws JSONException {
        int divisions = 0;
        if(leagueJson.has("divisions")){
            divisions = leagueJson.getInt("divisions");
        }
        return new LeagueDbItem(leagueJson.getString("league_id"),
                leagueJson.getInt("total_rosters"),
                leagueJson.getString("status"), divisions,
                leagueJson.getString("season_type"), leagueJson.getString("season"),
                Utilities.jsonArrayToStringArray(leagueJson.getJSONArray("roster_positions")),
                leagueJson.getString("previous_league_id"), leagueJson.getString("name"),
                leagueJson.getString("draft_id"), leagueJson.getString("avatar"));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(leagueId);
        parcel.writeInt(total_rosters);
        parcel.writeString(status);
        parcel.writeInt(divisions);
        parcel.writeString(season_type);
        parcel.writeString(season);
        parcel.writeStringList(roster_positions);
        parcel.writeString(previous_league_id);
        parcel.writeString(name);
        parcel.writeString(draft_id);
        parcel.writeString(avatar);
    }
}
