package edu.neu.cs5520sp22.fantasyleaguecompanion.roster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.SettingsActivity;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApi;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueDbItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.Player;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.PlayerRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters.RosterRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters.Rosters;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ImageStore;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApiHelper;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

public class Roster extends AppCompatActivity {
    ProgressBar circular;
    PlayerRepository playerRepository;
    RosterRepository rosterRepository;
    SharedPreferences p;
    PlayerAdapter playerAdapter;
    ArrayList<PlayerRow> players = new ArrayList<>();
    String userId;
    String userName;
    NFLState nflState;
    LeagueDbItem leagueDbItem;
    ActionBar actionBar;
    private final Handler rosterHandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);


        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#37003C"));

        // Set BackgroundDrawable
        //actionBar.setTitle("Select a League");
        actionBar.setBackgroundDrawable(colorDrawable);
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("username");
        nflState = getIntent().getParcelableExtra("nflState");
        leagueDbItem = getIntent().getParcelableExtra("league");
        Log.d("Roster", leagueDbItem.getLeagueId() + " " + userId);
        circular = findViewById(R.id.circularRoster);
        circular.setVisibility(View.INVISIBLE);
        playerRepository = new PlayerRepository(this.getApplication());
        rosterRepository = new RosterRepository(this.getApplication());
        p = PreferenceManager.getDefaultSharedPreferences(this);
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.team);
        recyclerView.setHasFixedSize(true);
        playerAdapter = new PlayerAdapter(players);
        recyclerView.setAdapter(playerAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
        updateRoster();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBar.setTitle(userName + " Roster");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu) {
            startActivity(new Intent(Roster.this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.logout) {
            Utilities.logOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateRoster(){
        circular.setVisibility(View.VISIBLE);
        new Thread(new rosterThread(this)).start();
    }

    private void addItems(){
        playerAdapter.notifyItemInserted(players.size() -  1);
    }

    class rosterThread implements Runnable{

        private Context context;

        rosterThread(Context context) {
            this.context = context;
        }

        private void displayPlayers(ArrayList<String> roster, String rosterPart) {
            if(roster != null) {
                String rosterSpot = "BN";
                ArrayList<String> positions = new ArrayList<>();
                if (rosterPart.equals("starters")) {
                    positions = (ArrayList<String>) leagueDbItem.getRoster_positions().clone();
                    positions.removeAll(Collections.singleton("BN"));
                }
                if (rosterPart.equals("taxi")) {
                    rosterSpot = "TX";
                }
                if (rosterPart.equals("reserve")) {
                    rosterSpot = "IR";
                }
                Log.d("start", "getPlayers");
                for (String p : roster) {
                    Log.d("player", p);
                    if (!p.equals("0")) {
                        Player dbP = playerRepository.findPlayerById(p);
                        if (rosterPart.equals("starters")) {
                            rosterSpot = Utilities.rosterView(positions.remove(0));
                        }
                        Bitmap image;
                        if (dbP.getPosition().equals("DEF")) {
                            image = ImageStore.loadTeamImage(context, dbP.getSleeperId());
                        } else {
                            image = ImageStore.loadPlayerImage(context, dbP.getSleeperId());
                        }
                        PlayerRow pRow = new PlayerRow(dbP.getFull_name(), rosterSpot, image, dbP.getPosition(), dbP.getTeam());
                        players.add(pRow);
                    } else {
                        players.add(PlayerRow.EmptySpot(rosterSpot));
                    }
                }
            }
        }

        private void getRosterCurrentYear(){
            JSONArray rosters = SleeperApi.getLeagueRosters(leagueDbItem.getLeagueId());
            try {
                HashMap<String, ArrayList<String>> roster = SleeperApiHelper.getRosterParts(SleeperApiHelper.getRosterFromLeague(rosters, userId));
                for (String part : Utilities.ROSTER_ORDER) {
                    Log.d("Roster", part);
                    displayPlayers(Objects.requireNonNull(roster.get(part)), part);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void getRosterPreviousYear(){
            SleeperApiHelper.saveRosters(rosterRepository, leagueDbItem);
            buildRoster(rosterRepository.findRosterByUser(leagueDbItem.getLeagueId(), userId));
        }

        private void buildRoster(Rosters roster){
            displayPlayers(roster.getStarters(), "starters");
            displayPlayers(roster.getBench(), "bench");
            displayPlayers(roster.getReserve(), "reserve");
            displayPlayers(roster.getTaxi(), "taxi");

        }

        @Override
        public void run() {
            Thread t = new Thread(() -> SleeperApiHelper.getPlayers(playerRepository, p));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!leagueDbItem.getStatus().equals("complete")) {
                getRosterCurrentYear();
            }
            else {
                getRosterPreviousYear();
            }
            rosterHandle.post(()-> {
                addItems();
                circular.setVisibility(View.INVISIBLE);
            });
        }
    }
}