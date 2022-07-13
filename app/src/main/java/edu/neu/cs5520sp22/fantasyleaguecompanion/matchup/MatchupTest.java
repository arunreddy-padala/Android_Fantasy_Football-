package edu.neu.cs5520sp22.fantasyleaguecompanion.matchup;

import static edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups.Matchup.buildMatchup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApi;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApiHelper;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueDbItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueUser;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups.Matchup;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups.MatchupRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.PlayerRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ImageStore;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;

public class MatchupTest extends AppCompatActivity {
    ProgressBar circular;
    MatchupRepository matchupRepository;
    LeagueRepository leagueRepository;
    MatchupAdapter matchupAdapter;
    PlayerRepository playerRepository;
    String userId;
    NFLState nflState;
    LeagueDbItem leagueDbItem;
    String weekNumber;
    boolean flag;
    Matchup matchupdbItem;
    TextView weekText;
    ArrayList<MatchupRow> matchupRows = new ArrayList<>();
    private final Handler matchupHandler = new Handler();
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_test);

        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#37003C"));

        // Set BackgroundDrawable

        actionBar.setBackgroundDrawable(colorDrawable);
        weekNumber = "1";
        final Spinner spinner = findViewById(R.id.weekSelector);
        String[] username = new String[]{
                "Select a Week",
                "Week 1",
                "Week 2",
                "Week 3",
                "Week 4",
                "Week 5",
                "Week 6",
                "Week 7",
                "Week 8",
                "Week 9",
                "Week 10",
                "Week 11",
                "Week 12",
                "Week 13",
                "Week 14"
        };

        final List<String> userList = new ArrayList<>(Arrays.asList(username));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, userList);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    flag = true;
                    weekNumber = String.valueOf(selectedItemText.charAt(selectedItemText.length()-1));
                    matchupRows.clear();
                    resetupRecyclerView();
                    matchupBuilder();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        nflState = getIntent().getParcelableExtra("nflState");
        leagueDbItem = getIntent().getParcelableExtra("league");
        circular = findViewById(R.id.circularMatchup);
        matchupRepository = new MatchupRepository(this.getApplication());
        leagueRepository = new LeagueRepository(this.getApplication());
        playerRepository = new PlayerRepository(this.getApplication());
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.matchup_recyclerview);
        recyclerView.setHasFixedSize(true);
        matchupAdapter = new MatchupAdapter(matchupRows);
        recyclerView.setAdapter(matchupAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
        matchupBuilder();
        Log.d("Matchup","After builder");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //setTitle(leagueDbItem.getName());
        actionBar.setTitle(leagueDbItem.getName());
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    private void resetupRecyclerView() {
        nflState = getIntent().getParcelableExtra("nflState");
        leagueDbItem = getIntent().getParcelableExtra("league");
        circular = findViewById(R.id.circularMatchup);
        matchupRepository = new MatchupRepository(this.getApplication());
        leagueRepository = new LeagueRepository(this.getApplication());
        playerRepository = new PlayerRepository(this.getApplication());
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.matchup_recyclerview);
        recyclerView.setHasFixedSize(true);
        matchupAdapter = new MatchupAdapter(matchupRows);
        recyclerView.setAdapter(matchupAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    private void matchupBuilder() {
        circular.setVisibility(View.VISIBLE);
        Log.d("Matchup","In matchupBuilder");
        matchupThread1 m1 = new matchupThread1(this);
        Log.d("Matchup","After matchup thread");
        new Thread(m1).start();
        Log.d("Matchup","After thread");
    }

//    private void updateMatchup1() {
//        circular.setVisibility(View.VISIBLE);
//        new Thread(new matchupThread1(this)).start();
//    }
    private void addItems1() {
        Log.d("Matchup","In addtems : "+matchupRows.size());
        matchupAdapter.notifyItemInserted(matchupRows.size());
    }


    class matchupThread1 implements Runnable{
        private JSONArray matchupInfo;
        private String leagueId;
        private int week;
        private Context context;

        matchupThread1(Context context){
            this.context = context;
            Log.d("Matchup","In thread");
            try{
                leagueId=leagueDbItem.getLeagueId();
                //week = nflState.getWeek();
                week = 1;
                Log.d("Matchup","In in try catch");
                SleeperApiHelper.getLeagueUsers(leagueRepository,leagueId);
                Log.d("Matchup","league id: "+leagueDbItem.getLeagueId()+" week: "+weekNumber);
                //matchupInfo = SleeperApi.getLeagueMatchups(leagueDbItem.getLeagueId(), String.valueOf(week));
                matchupInfo = SleeperApi.getLeagueMatchups(leagueDbItem.getLeagueId(), weekNumber);
                Log.d("Matchup","In thread matchup info size: "+matchupInfo.length());
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private List<Matchup> builldMatchups() {
            Log.d("Matchup","In buildmatchups");
            List<Matchup> matchupList = new ArrayList<>();
            for (int i = 0; i < matchupInfo.length(); i++) {
                try {
                    JSONObject matchup = matchupInfo.getJSONObject(i);
                    matchupList.add(buildMatchup(matchup,leagueId,week));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Log.d("Matchup","In buildmatchups matchup list size: "+matchupList.size());
            return matchupList;
        }

        @Override
        public void run(){
            Log.d("Matchup","In run");
            List<Matchup> matchups = builldMatchups();
            showMatchups(matchups, matchupInfo);
            matchupHandler.post(()-> {

                addItems1();
                Log.d("Matchup","After addtems");
                circular.setVisibility(View.INVISIBLE);
                Log.d("Matchup","After cicular");
            });

        }

        private void showMatchups(List<Matchup> matchups, JSONArray matchupInfo) {
            Log.d("Matchup","In showmatchups "+matchups.size());
            List<Integer> visitedMatchupsRoster = new ArrayList<>();
            for(int i=0; i<matchups.size();i++){
                Log.d("Matchup","Inside For");
                Matchup m = matchups.get(i);
                visitedMatchupsRoster.add(m.getRoster_id());
                //MatchupRow(Bitmap userimage1, Bitmap userimage2, String username1, String username2,
                        //String teamname1, String teamname2, String points1, String points2)
                //player_image.setImageBitmap(ImageStore.loadTeamImage(this, pick.getImage()));z
                for(int j=1; j<matchups.size();j++){
                    Log.d("Matchup","Inside second For");
                    if(!visitedMatchupsRoster.contains(matchups.get(j).getRoster_id())){
                        if(m.getMatchup_id() == matchups.get(j).getMatchup_id() ){
                            Matchup m2 = matchups.get(j);
                            LeagueUser player1 = leagueRepository.findLeagueUserByRosterId(leagueId,m.getRoster_id());
                            LeagueUser player2 = leagueRepository.findLeagueUserByRosterId(leagueId,m2.getRoster_id());
                            Log.d("Matchup","player 1"+player1.getDisplayName()+"Player 2:"+player2.getDisplayName()+"Avatar: "+player2.getUserAvatar());
                            MatchupRow  mrow = new MatchupRow(ImageStore.loadUserImage(context,player1.getUserAvatar()),
                                    ImageStore.loadUserImage(context,player2.getUserAvatar()),
                                    player1.getDisplayName(), player2.getDisplayName(),
                                    m.getPoints(),m2.getPoints());
//                            MatchupRow  mrow = new MatchupRow(ImageStore.loadUserImage(context,player1.getUserAvatar()),
//                                    ImageStore.loadUserImage(context,player2.getUserAvatar()),
//                                    player1.getDisplayName(),player2.getDisplayName(),
//                                    player1.getDisplayName(), player2.getDisplayName(),
//                                    m.getPoints(),m2.getPoints());
                            matchupRows.add(mrow);
                        }
                    }

                }

            }
        }
    }

}