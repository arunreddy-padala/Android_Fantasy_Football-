package edu.neu.cs5520sp22.fantasyleaguecompanion.standings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.SettingsActivity;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApi;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApiHelper;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueDbItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueUser;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters.RosterRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.rosters.Rosters;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

public class Standings extends AppCompatActivity {
    ProgressBar circular;
    NFLState nflState;
    LeagueDbItem leagueDbItem;
    LeagueRepository leagueRepository;
    RosterRepository rosterRepository;
    StandingAdapter standingAdapter;
    ArrayList<StandingItem> standingItems = new ArrayList<>();
    private final Handler standingHandle = new Handler();
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#37003C"));

        // Set BackgroundDrawable
        //actionBar.setTitle("Select a League");
        actionBar.setBackgroundDrawable(colorDrawable);
        circular = findViewById(R.id.circularStandings);
        circular.setVisibility(View.INVISIBLE);
        nflState = getIntent().getParcelableExtra("nflState");
        leagueDbItem = getIntent().getParcelableExtra("league");
        leagueRepository = new LeagueRepository(this.getApplication());
        rosterRepository = new RosterRepository(this.getApplication());
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.standings);
        recyclerView.setHasFixedSize(true);
        standingAdapter = new StandingAdapter(standingItems);
        recyclerView.setLayoutManager(rLayoutManger);
        recyclerView.setAdapter(standingAdapter);
        buildTitle();
        new Thread(new standingsThread(this)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBar.setTitle(leagueDbItem.getName() + " Standings");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu) {
            startActivity(new Intent(Standings.this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.logout) {
            Utilities.logOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildTitle(){
        View itemView = findViewById(R.id.standingTitle);
        TextView name = itemView.findViewById(R.id.standing_name);
        TextView wins = itemView.findViewById(R.id.standing_wins);
        TextView losses = itemView.findViewById(R.id.standing_losses);
        TextView ties = itemView.findViewById(R.id.standing_ties);
        TextView ppts = itemView.findViewById(R.id.standing_ppts);
        TextView pts = itemView.findViewById(R.id.standing_pts);
        TextView pts_against = itemView.findViewById(R.id.standing_pts_against);
        name.setText(R.string.standings_name);
        wins.setText(R.string.standings_w);
        losses.setText(R.string.standings_l);
        ties.setText(R.string.standings_t);
        ppts.setText(R.string.standings_maxpf);
        pts.setText(R.string.standings_pf);
        pts_against.setText(R.string.standings_pa);
        wins.setOnClickListener(view -> sortWins());
        losses.setOnClickListener(view -> sortLosses());
        ties.setOnClickListener(view -> sortTies());
        ppts.setOnClickListener(view -> sortMaxPF());
        pts.setOnClickListener(view -> sortPF());
        pts_against.setOnClickListener(view -> sortPA());
        name.setOnClickListener(view -> sortName());
    }

    private void sortName(){
        Log.d("sort", "Name");
        standingItems.sort(Comparator.comparing(StandingItem::getName));
        standingAdapter.notifyDataSetChanged();
    }

    private void sortWins(){
        Log.d("sort", "wins");
        standingItems.sort(Comparator.comparing(standingItem -> -Integer.parseInt(standingItem.getWins())));
        standingAdapter.notifyDataSetChanged();
    }

    private void sortLosses(){
        Log.d("sort", "losses");
        standingItems.sort(Comparator.comparing(standingItem -> -Integer.parseInt(standingItem.getLosses())));
        standingAdapter.notifyDataSetChanged();
    }

    private void sortTies(){
        Log.d("sort", "ties");
        standingItems.sort(Comparator.comparing(standingItem -> -Integer.parseInt(standingItem.getTies())));
        standingAdapter.notifyDataSetChanged();
    }

    private void sortPF(){
        Log.d("sort", "PF");
        standingItems.sort(Comparator.comparing(standingItem -> -Double.parseDouble(standingItem.getPts())));
        standingAdapter.notifyDataSetChanged();
    }

    private void sortPA(){
        Log.d("sort", "PA");
        standingItems.sort(Comparator.comparing(standingItem -> -Double.parseDouble(standingItem.getPts_against())));
        standingAdapter.notifyDataSetChanged();
    }

    private void sortMaxPF(){
        Log.d("sort", "MaxPF");
        standingItems.sort(Comparator.comparing(standingItem -> -Double.parseDouble(standingItem.getPpts())));
        standingAdapter.notifyDataSetChanged();
    }


    class standingsThread implements Runnable{
        Context context;

        public standingsThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            try {
                if(!leagueDbItem.getStatus().equals("complete")) {
                    getStandingsCurrentYear();
                }
                else {
                    getStandingsPreviousYear();
                }
                standingHandle.post(() -> {
                    sortName();
                    standingAdapter.notifyItemInserted(standingItems.size() - 1);
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void getStandingsCurrentYear() throws JSONException {
            JSONArray standings = SleeperApi.getLeagueRosters(leagueDbItem.getLeagueId());
            for (int i = 0; i < standings.length(); i++) {
                JSONObject roster = standings.getJSONObject(i);
                LeagueUser user = leagueRepository.findLeagueUserById(leagueDbItem.getLeagueId(), roster.getString("owner_id"));
                standingItems.add(StandingItem.Builder(context, roster, user));
                Log.d("add standing", roster.getString("owner_id"));
            }
        }

        private void getStandingsPreviousYear() {
            SleeperApiHelper.saveRosters(rosterRepository, leagueDbItem);
            List<Rosters> rosters = rosterRepository.findRostersforLeague(leagueDbItem.getLeagueId());
            for (Rosters roster : rosters) {
                LeagueUser user = leagueRepository.findLeagueUserById(leagueDbItem.getLeagueId(), roster.getOwner_id());
                standingItems.add(StandingItem.Builder(context, roster, user));
                Log.d("add standing", roster.getOwner_id());
            }
        }
    }
}