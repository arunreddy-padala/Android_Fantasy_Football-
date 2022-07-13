package edu.neu.cs5520sp22.fantasyleaguecompanion.matchup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApi;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApiHelper;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueDbItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups.Matchup;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups.MatchupRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.Player;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.PlayerRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters.Rosters;
import edu.neu.cs5520sp22.fantasyleaguecompanion.roster.PlayerAdapter;
import edu.neu.cs5520sp22.fantasyleaguecompanion.roster.PlayerRow;
import edu.neu.cs5520sp22.fantasyleaguecompanion.roster.Roster;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ImageStore;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

public class MatchupUI extends AppCompatActivity {
    ProgressBar circular;
    PlayerRepository playerRepository;
    MatchupRepository matchupRepository;
    SharedPreferences m;
    PlayerAdapter playerAdapter;
    MatchupAdapter matchupAdapter;
    ArrayList<PlayerRow> players = new ArrayList<>();
    String userId;
    NFLState nflState;
    LeagueDbItem leagueDbItem;
    Matchup matchupdbItem;
    ArrayList<MatchupRow> matchups = new ArrayList<>();

    private final Handler matchupHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_ui);
        userId = getIntent().getStringExtra("userId");
        nflState = getIntent().getParcelableExtra("nflState");
        leagueDbItem = getIntent().getParcelableExtra("league");
        matchupdbItem = getIntent().getParcelableExtra("matchups");

        Log.d("Matchup", matchupdbItem.getMatchup_id() + " " + userId);
        circular = findViewById(R.id.circularMatchup);
        circular.setVisibility(View.INVISIBLE);
        playerRepository = new PlayerRepository(this.getApplication());
        matchupRepository = new MatchupRepository(this.getApplication());
        m = PreferenceManager.getDefaultSharedPreferences(this);
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.matchup_ui);
        recyclerView.setHasFixedSize(true);
        playerAdapter = new PlayerAdapter(players);
        matchupAdapter = new MatchupAdapter(matchups);
        recyclerView.setAdapter(matchupAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
        updateMatchup();


    }

    private void updateMatchup() {
        circular.setVisibility(View.VISIBLE);
        new Thread(new matchupThread(this)).start();
    }
    private void addItems() {
        matchupAdapter.notifyItemInserted(matchups.size() - 1);
    }

    class matchupThread implements Runnable{
        private Context context;

        matchupThread(Context context){
            this.context = context;
            //SleeperApiHelper.getT
        }


//        private void displayMatchups(ArrayList<String> matchups, String matchupPart) {
//
//            Log.d("start", "getMatchup");
//            for (String m : matchups ) {
//                Log.d("matchup", m);
//                if (!m.equals("0")) {
//                    Player dbP = playerRepository.findPlayerById(p);
//                    if (rosterPart.equals("starters")) {
//                        rosterSpot = Utilities.rosterView(positions.remove(0));
//                    }
//                    Bitmap image;
//                    if (dbP.getPosition().equals("DEF")) {
//                        image = ImageStore.loadTeamImage(context, dbP.getSleeperId());
//                    } else {
//                        image = ImageStore.loadPlayerImage(context, dbP.getSleeperId());
//                    }
//                    PlayerRow pRow = new PlayerRow(dbP.getFull_name(), rosterSpot, image, dbP.getPosition(), dbP.getTeam());
//                    players.add(pRow);
//                }
//                else {
//                    players.add(PlayerRow.EmptySpot(rosterSpot));
//                }
//            }
//        }

//        private void getRosterCurrentYear(){
//            JSONArray rosters = SleeperApi.getLeagueRosters(leagueDbItem.getLeagueId());
//            try {
//                HashMap<String, ArrayList<String>> roster = SleeperApiHelper.getRosterParts(SleeperApiHelper.getRosterFromLeague(rosters, userId));
//                for (String part : Utilities.ROSTER_ORDER) {
//                    Log.d("Roster", part);
//                    displayPlayers(Objects.requireNonNull(roster.get(part)), part);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void getRosterPreviousYear(){
//            //TODO save roster to DB
//            List<Rosters> rosters = rosterRepository.findRostersforLeague(leagueDbItem.getLeagueId());
//            if (rosters.size() != leagueDbItem.getTotal_rosters()){
//                saveRosters();
//            }
//            buildRoster(rosterRepository.findRosterByUser(leagueDbItem.getLeagueId(), userId));
//        }

        private void saveMatchups(){
            JSONArray matchups = SleeperApi.getLeagueMatchups(String.valueOf(matchupdbItem.getMatchup_id()),String.valueOf(nflState.getWeek()));
            Log.d("saveMatchups", String.valueOf(matchupdbItem.getMatchup_id()));
            for (int i = 0; i < matchups.length(); i++) {
                try {
                    //TODO hardcoded week as 1 --> change it later
                    Matchup matchup = Matchup.buildMatchup(matchups.getJSONObject(i),leagueDbItem.getLeagueId(),nflState.getWeek());
                    matchupRepository.insertMatchup(matchup);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

//        private void buildMatchup(Matchup matchup){
//            displayMatchups(matchup.getStarters(), "starters");
//        }

        @Override
        public void run() {
            saveMatchups();
            matchupHandler.post(()-> {
                addItems();
                circular.setVisibility(View.INVISIBLE);
            });
        }
    }
}