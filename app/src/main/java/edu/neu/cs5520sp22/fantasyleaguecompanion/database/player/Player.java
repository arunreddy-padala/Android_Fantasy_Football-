package edu.neu.cs5520sp22.fantasyleaguecompanion.database.player;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "players")
public class Player {
    @PrimaryKey
    private @NonNull String sleeperId;
    private int espn_id;
    private String college;
    private String weight;
    private int age;
    private int years_exp;
    private int number;
    private String height;
    private String team;
    private String full_name;
    private String position;
    //private ArrayList<String> fantasy_positions;
    private String birth_date;
    private boolean active;
    private String last_name;
    private String status;
    private String first_name;

    public Player(@NonNull String sleeperId, int espn_id, String college, String weight, int age, int years_exp,
                  int number, String height, String team, String full_name, String position,
                  //ArrayList<String> fantasy_positions,
                  String birth_date, boolean active,
                  String last_name, String status, String first_name) {
        this.sleeperId = sleeperId;
        this.espn_id = espn_id;
        this.college = college;
        this.weight = weight;
        this.age = age;
        this.years_exp = years_exp;
        this.number = number;
        this.height = height;
        this.team = team;
        this.full_name = full_name;
        this.position = position;
        //this.fantasy_positions = fantasy_positions;
        this.birth_date = birth_date;
        this.active = active;
        this.last_name = last_name;
        this.status = status;
        this.first_name = first_name;
    }

    @NonNull
    public String getSleeperId() {
        return sleeperId;
    }

    public void setSleeperId(@NonNull String sleeperId) {
        this.sleeperId = sleeperId;
    }

    public int getEspn_id() {
        return espn_id;
    }

    public void setEspn_id(int espn_id) {
        this.espn_id = espn_id;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getYears_exp() {
        return years_exp;
    }

    public void setYears_exp(int years_exp) {
        this.years_exp = years_exp;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
/**
    public ArrayList<String> getFantasy_positions() {
        return fantasy_positions;
    }

    public void setFantasy_positions(ArrayList<String> fantasy_positions) {
        this.fantasy_positions = fantasy_positions;
    }
*/
    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public static Player PlayerBuilder(JSONObject player) throws JSONException {
        // Default values for if null
        int espn_id = 0;
        String college = "";
        String weight = "";
        int age = 0;
        int years_exp = 0;
        int number = -1;
        String height = "";
        String team = "";
        String full_name = "";
        String position = "";
        String birth_date = "";
        boolean active = false;
        String last_name = "";
        String status = "";
        String first_name = "";
        if (!player.isNull("espn_id")) {
            espn_id = player.getInt("espn_id");
        }
        if (!player.isNull("college")) {
            college = player.getString("college");
        }
        if (!player.isNull("weight")) {
            weight = player.getString("weight");
        }
        if (!player.isNull("age")) {
            age = player.getInt("age");
        }
        if (!player.isNull("years_exp")) {
            years_exp = player.getInt("years_exp");
        }
        if (!player.isNull("number")) {
            number = player.getInt("number");
        }
        if (!player.isNull("height")) {
            height = player.getString("height");
        }
        if (!player.isNull("team")) {
            team = player.getString("team");
        }
        if (!player.isNull("full_name")) {
            full_name = player.getString("full_name");
        }
        if (!player.isNull("position")) {
            position = player.getString("position");
        }
        if (!player.isNull("birth_date")) {
            birth_date = player.getString("birth_date");
        }
        if (!player.isNull("active")) {
            active = player.getBoolean("active");
        }
        if (!player.isNull("last_name")) {
            last_name = player.getString("last_name");
        }
        if (!player.isNull("status")) {
            status = player.getString("status");
        }
        if (!player.isNull("first_name")) {
            first_name = player.getString("first_name");
        }
        return new Player(player.getString("player_id"), espn_id, college,
                weight, age, years_exp, number, height, team, full_name,position, birth_date,
                active, last_name, status, first_name);
    }
}
