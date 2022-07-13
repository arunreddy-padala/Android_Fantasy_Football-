package edu.neu.cs5520sp22.fantasyleaguecompanion.league;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.SettingsActivity;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueDbItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.draft.Draft;
import edu.neu.cs5520sp22.fantasyleaguecompanion.matchup.MatchupTest;
import edu.neu.cs5520sp22.fantasyleaguecompanion.matchup.MatchupUI;
import edu.neu.cs5520sp22.fantasyleaguecompanion.roster.Roster;
import edu.neu.cs5520sp22.fantasyleaguecompanion.standings.Standings;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ImageStore;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ItemClickListener;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApiHelper;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

public class League extends AppCompatActivity {
    LeagueAdapter adapter;
    ArrayList<LeagueItem> users = new ArrayList<>();
    LeagueRepository leagueRepository;
    private final Handler apiHandler = new Handler();
    ProgressBar circular;
    LeagueDbItem leagueDbItem;
    NFLState nflState;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);

        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#37003C"));

        // Set BackgroundDrawable

        actionBar.setBackgroundDrawable(colorDrawable);
        circular = findViewById(R.id.circularLeague);
        nflState = getIntent().getParcelableExtra("nflState");
        leagueDbItem = getIntent().getParcelableExtra("league");
        RecyclerView recyclerView = findViewById(R.id.rosters);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rLayoutManger);
        ItemClickListener listener = this::loadUserRosters;
        adapter = new LeagueAdapter(users, listener);
        recyclerView.setAdapter(adapter);
        leagueRepository = new LeagueRepository(this.getApplication());
        leagueThread thread = new leagueThread(this);
        new Thread(thread).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //setTitle(leagueDbItem.getName());
        actionBar.setTitle(leagueDbItem.getName());
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu) {
            startActivity(new Intent(League.this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.logout) {
            Utilities.logOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUserRosters(String userId, String userName){
        Intent intent = new Intent(this, Roster.class);
        intent.putExtra("userId", userId);
        intent.putExtra("username", userName);
        intent.putExtra("nflState", nflState);
        intent.putExtra("league", leagueDbItem);
        startActivity(intent);
    }

    public void loadStandings(View view){
        if(leagueDbItem.getStatus().equals("in_season") || leagueDbItem.getStatus().equals("complete")) {
            Intent intent = new Intent(this, Standings.class);
            intent.putExtra("nflState", nflState);
            intent.putExtra("league", leagueDbItem);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "League is not in season", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickDraft(View view){
        if(!leagueDbItem.getStatus().equals("pre_draft")) {
            Intent intent = new Intent(this, Draft.class);
            intent.putExtra("nflState", nflState);
            intent.putExtra("league", leagueDbItem);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "League is Pre-draft", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickMatchup(View view){
        if(!leagueDbItem.getStatus().equals("pre_draft")) {
            Intent intent = new Intent(this, MatchupTest.class);
            intent.putExtra("nflState", nflState);
            intent.putExtra("league", leagueDbItem);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "League is Pre-Matchup", Toast.LENGTH_SHORT).show();

        }
//        if(!leagueDbItem.getStatus().equals("matchups")) {
//            Intent intent = new Intent(this, MatchupUI.class);
//            intent.putExtra("nflState", nflState);
//            intent.putExtra("league", leagueDbItem);
//            startActivity(intent);
//        }
//        else{
//            Toast.makeText(this, "Error in Matchup creation", Toast.LENGTH_SHORT).show();
//        }
    }

    private void updateAdapter(){
        adapter.notifyItemInserted(users.size() - 1);
    }

    class leagueThread implements Runnable{

        Context context;

        leagueThread(Context context){
            this.context = context;
        }

        @Override
        public void run() {
            try {
                users.addAll(Utilities.convertFromDb(SleeperApiHelper.getLeagueUsers(leagueRepository, leagueDbItem.getLeagueId()), context));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateAdapter();
            apiHandler.post(() -> circular.setVisibility(View.INVISIBLE));
            Log.d("finished", String.valueOf(users.size()));
        }
    }

    public void getPreviousLeagues(View view){
        try {
            ArrayList<LeagueItem> previousLeagues = new ArrayList<>();
            HashMap<String, LeagueDbItem> previousHash = new HashMap<>();
            if (!leagueDbItem.getPrevious_league_id().equals("null")){
                while (!leagueDbItem.getPrevious_league_id().equals("null")){
                    leagueDbItem = SleeperApiHelper.getLeague(leagueRepository, leagueDbItem.getPrevious_league_id());
                    previousLeagues.add(new LeagueItem(leagueDbItem.getLeagueId(),
                            leagueDbItem.getSeason() + " " + leagueDbItem.getName(),
                            ImageStore.loadUserImage(this, leagueDbItem.getAvatar())));
                    previousHash.put(leagueDbItem.getLeagueId(), leagueDbItem);
                    Log.d("previous league", leagueDbItem.getLeagueId());
                }
                Intent intent = new Intent(this, UserLeagues.class);
                intent.putParcelableArrayListExtra("leagues", previousLeagues);
                intent.putExtra("leaguesHash", previousHash);
                intent.putExtra("nflState", nflState);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "League has no history", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}