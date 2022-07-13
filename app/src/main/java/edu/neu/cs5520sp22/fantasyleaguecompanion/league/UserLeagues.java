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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.SettingsActivity;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApi;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueDbItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ImageStore;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ItemClickListener;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

public class UserLeagues extends AppCompatActivity {

    LeagueAdapter adapter;
    ArrayList<LeagueItem> leagues = new ArrayList<>();
    HashMap<String, LeagueDbItem> leagueDbItemHashMap = new HashMap<>();
    String userId;
    NFLState nflState;
    LeagueRepository leagueRepository;
    private final Handler apiHandler = new Handler();
    ProgressBar circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_leagues);
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#37003C"));

        // Set BackgroundDrawable
        actionBar.setTitle("Select a League");
        actionBar.setBackgroundDrawable(colorDrawable);
        circular = findViewById(R.id.circularUser);
        nflState = getIntent().getParcelableExtra("nflState");
        userId = getIntent().getStringExtra("user_id");
        RecyclerView recyclerView = findViewById(R.id.leagues);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rLayoutManger);
        ItemClickListener listener = this::loadLeague;
        adapter = new LeagueAdapter(leagues, listener);
        recyclerView.setAdapter(adapter);
        leagueRepository = new LeagueRepository(this.getApplication());
        if(getIntent().hasExtra("leagues")){
            leagues.addAll(getIntent().getParcelableArrayListExtra("leagues"));
            leagueDbItemHashMap.putAll((HashMap) getIntent().getSerializableExtra("leaguesHash"));
            updateAdapter();
            circular.setVisibility(View.INVISIBLE);
        }
        else {
            leagueThread thread = new leagueThread(this);
            new Thread(thread).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  setTitle("Leagues");

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu) {
            startActivity(new Intent(UserLeagues.this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.logout) {
            Utilities.logOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLeague(String leagueId, String leagueName){
        Intent intent = new Intent(this, League.class);
        intent.putExtra("league", leagueDbItemHashMap.get(leagueId));
        intent.putExtra("nflState", nflState);
        startActivity(intent);
    }

    private void updateAdapter(){
        adapter.notifyItemInserted(leagues.size() - 1);
    }


    class leagueThread implements Runnable{
        Context context;

        public leagueThread(Context context) {
            this.context = context;
        }

        private void getLeagues(){
            JSONArray leaguesJson = SleeperApi.getLeagues(userId, nflState);
            Log.d("get leagues", leaguesJson.toString());
            for (int i = 0; i < leaguesJson.length(); i++) {
                try {
                    JSONObject league = leaguesJson.getJSONObject(i);
                    Log.d("add league", league.toString());
                    leagueDbItemHashMap.put(league.getString("league_id"), LeagueDbItem.buildLeague(league));
                    leagues.add(new LeagueItem(league.getString("league_id"),
                            league.getString("name"),
                            ImageStore.loadUserImage(context, league.getString("avatar"))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            getLeagues();
            updateAdapter();
            apiHandler.post(() -> circular.setVisibility(View.INVISIBLE));
            Log.d("finished", String.valueOf(leagues.size()));
        }
    }
}