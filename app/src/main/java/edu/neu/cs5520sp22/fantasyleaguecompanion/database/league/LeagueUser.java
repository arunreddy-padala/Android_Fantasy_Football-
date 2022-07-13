package edu.neu.cs5520sp22.fantasyleaguecompanion.database.league;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "leagueUsers", primaryKeys = {"leagueId", "userId"})
public class LeagueUser {
    private @NonNull String leagueId;
    private @NonNull String userId;
    private String displayName;
    private String avatarUrl;
    private String userAvatar;
    private int roster_id;

    public LeagueUser(@NonNull String leagueId, @NonNull String userId,
                      String displayName, String avatarUrl, String userAvatar, int roster_id) {
        this.leagueId = leagueId;
        this.userId = userId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.userAvatar = userAvatar;
        this.roster_id = roster_id;
    }

    @NonNull
    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(@NonNull String leagueId) {
        this.leagueId = leagueId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getRoster_id() {
        return roster_id;
    }

    public void setRoster_id(int roster_id) {
        this.roster_id = roster_id;
    }
}
