package edu.neu.cs5520sp22.fantasyleaguecompanion.draft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.SettingsActivity;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApi;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.draft.DraftPick;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.draft.DraftPickRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueDbItem;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.league.LeagueUser;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ImageStore;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;
import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApiHelper;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

public class Draft extends AppCompatActivity {
    DraftPickRepository draftPickRepository;
    LeagueRepository leagueRepository;
    private final Handler draftHandle = new Handler();
    ProgressBar circular;
    NFLState nflState;
    LeagueDbItem leagueDbItem;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);


        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#37003C"));

        // Set BackgroundDrawable

        actionBar.setBackgroundDrawable(colorDrawable);
        nflState = getIntent().getParcelableExtra("nflState");
        leagueDbItem = getIntent().getParcelableExtra("league");
        circular = findViewById(R.id.circularDraft);
        draftPickRepository = new DraftPickRepository(this.getApplication());
        leagueRepository = new LeagueRepository(this.getApplication());
        draftBuilder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBar.setTitle(leagueDbItem.getName() + " Draft");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu) {
            startActivity(new Intent(Draft.this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.logout) {
            Utilities.logOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void draftBuilder(){
        new Thread(new draftThread()).start();
    }

    private void showDraft(HashMap<Integer, HashMap<Integer, DraftPick>> picks, JSONObject draftInfo){
        TableLayout tl = findViewById(R.id.linlay);
        int rounds = 0;
        int teams = 0;
        try {
            rounds = draftInfo.getJSONObject("settings").getInt("rounds");
            teams = draftInfo.getJSONObject("settings").getInt("teams");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TableRow tr = new TableRow(this);
        HashMap<Integer, DraftPick> users = picks.get(0);
        for (int i = 1; i <= teams; i++) {
            DraftPick pick = Objects.requireNonNull(users).get(i);
            View view = getLayoutInflater().inflate(R.layout.draft_card, (ViewGroup) tr.getRootView(), false);
            ImageView player_image = view.findViewById(R.id.draft_photo);
            TextView draft_slot = view.findViewById(R.id.draft_slot);
            TextView player_name = view.findViewById(R.id.draft_player_name);
            RelativeLayout card = view.findViewById(R.id.draft_card);
            card.setBackgroundColor(Color.GRAY);
            player_image.setImageBitmap(ImageStore.loadUserImage(this, Objects.requireNonNull(pick).getImage()));
            player_name.setText(pick.getName());
            draft_slot.setText("");
            tr.addView(view);
        }
        tl.addView(tr);
        for (int i = 1; i <= rounds; i++) {
            tr = new TableRow(this);
            HashMap<Integer, DraftPick> round = picks.get(i);
            for (int j = 1; j <= teams; j++) {
                DraftPick pick = Objects.requireNonNull(round).get(j);
                View view = getLayoutInflater().inflate(R.layout.draft_card, (ViewGroup) tr.getRootView(), false);
                ImageView player_image = view.findViewById(R.id.draft_photo);
                TextView draft_slot = view.findViewById(R.id.draft_slot);
                TextView player_name = view.findViewById(R.id.draft_player_name);
                RelativeLayout card = view.findViewById(R.id.draft_card);
                String slot = i + ".";
                if(j < 10){
                    slot = slot + "0" + j;
                }
                else {
                    slot = slot + j;
                }
                if(!Objects.requireNonNull(pick).getPicked_by().equals("")){
                    slot = slot + " - " + pick.getPicked_by();
                }
                if(pick.getPosition().equals("DEF")){
                    player_image.setImageBitmap(ImageStore.loadTeamImage(this, pick.getImage()));
                    draft_slot.setTextColor(Color.BLACK);
                    player_name.setTextColor(Color.BLACK);
                }
                else {
                    player_image.setImageBitmap(ImageStore.loadPlayerImage(this, pick.getImage()));
                }
                player_name.setText(pick.getName());
                draft_slot.setText(slot);
                card.setBackgroundColor(Color.parseColor(Utilities.RosterColor.valueOf(pick.getPosition()).color));
                tr.addView(view);
            }
            tl.addView(tr);
        }
    }

    class draftThread implements Runnable{
        private JSONObject draftInfo;

        draftThread(){
            try {
                SleeperApiHelper.getLeagueUsers(leagueRepository, leagueDbItem.getLeagueId());
                draftInfo = SleeperApi.getDraft(leagueDbItem.getDraft_id());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private HashMap<Integer, HashMap<Integer, DraftPick>> buildDraft(){
            int rounds = 0;
            int teams = 0;
            JSONObject order = new JSONObject();
            try {
                rounds = draftInfo.getJSONObject("settings").getInt("rounds");
                teams = draftInfo.getJSONObject("settings").getInt("teams");
                order = draftInfo.getJSONObject("slot_to_roster_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<Integer, HashMap<Integer, DraftPick>> picks = new HashMap<>();
            if(!draftPickRepository.draftExists(leagueDbItem.getDraft_id())){
                JSONArray draft = SleeperApi.getDraftPicks(leagueDbItem.getDraft_id());
                picks.put(0, new HashMap<>());
                for (int i = 1; i <= teams; i++) {
                    try {
                        int rosterId = order.getInt(String.valueOf(i));
                        LeagueUser user = leagueRepository.findLeagueUserByRosterId(leagueDbItem.getLeagueId(), rosterId);
                        String userAvatar = user.getUserAvatar();
                        DraftPick userSlot = DraftPick.ownerBuilder(leagueDbItem.getDraft_id(), i, userAvatar, user.getDisplayName());
                        Log.d("user slot", "Insert " + user.getDisplayName());
                        draftPickRepository.insertDraftPick(userSlot);
                        Objects.requireNonNull(picks.get(0)).put(i, userSlot);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //picks
                for (int i = 0; i < rounds * teams; i++) {
                    try {
                        JSONObject pick = draft.getJSONObject(i);
                        int curRound = pick.getInt("round");
                        int curPick = pick.getInt("draft_slot");
                        LeagueUser picker = leagueRepository.findLeagueUserById(leagueDbItem.getLeagueId(), pick.getString("picked_by"));
                        String picked_by = "";
                        if(picker.getRoster_id() != order.getInt(String.valueOf(curPick))){
                            picked_by = picker.getDisplayName();
                        }
                        JSONObject metadata = pick.getJSONObject("metadata");
                        String position = metadata.getString("position");
                        String name;
                        if(position.equals("DEF")){
                            name = metadata.getString("first_name") + " " + metadata.getString("last_name");
                        }
                        else{
                            name = metadata.getString("first_name") + " " + metadata.getString("last_name") + " (" + metadata.getString("team") + ")";
                        }
                        DraftPick pickSlot = new DraftPick(leagueDbItem.getDraft_id(), curRound, curPick, picked_by, metadata.getString("player_id"), name, position);
                        Log.d("player slot", "Insert " + name);
                        draftPickRepository.insertDraftPick(pickSlot);
                        if (!picks.containsKey(curRound)){
                            picks.put(curRound, new HashMap<>());
                        }
                        Objects.requireNonNull(picks.get(curRound)).put(curPick, pickSlot);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                for (int i = 0; i <= rounds; i++) {
                    picks.put(i, new HashMap<>());
                    for (int j = 1; j <= teams; j++) {
                        DraftPick pick = draftPickRepository.findDraftPick(leagueDbItem.getDraft_id(), i, j);
                        Objects.requireNonNull(picks.get(i)).put(j, pick);
                    }
                }
            }
            Log.d("map", String.valueOf(picks.size()));
            return picks;
        }

        @Override
        public void run() {
            HashMap<Integer, HashMap<Integer, DraftPick>> picks = buildDraft();
            draftHandle.post(()-> {
                showDraft(picks, draftInfo);
                circular.setVisibility(View.INVISIBLE);
            });
        }
    }
}