package edu.neu.cs5520sp22.fantasyleaguecompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApiHelper;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.PlayerRepository;
import edu.neu.cs5520sp22.fantasyleaguecompanion.league.UserLeagues;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.NFLState;

public class MainActivity extends AppCompatActivity {
    NFLState nflState;
    SharedPreferences preferences;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getNFLState();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user_id = preferences.getString("user_id", "");
        if(!user_id.equals("")){
            logIn(user_id);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }
        username = findViewById(R.id.usernameEdit);
        new Thread(() -> {
            PlayerRepository playerRepository = new PlayerRepository(this.getApplication());
            SharedPreferences p = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
            SleeperApiHelper.reloadPlayers(playerRepository, p);
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //setTitle("Main Screen");
       //getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLogIn(View v){
        String userNameEntered = username.getText().toString();
        if (!TextUtils.isEmpty(userNameEntered)) {
            try {
                String user_id = SleeperApiHelper.getUserId(userNameEntered);
                if (user_id != null){
                    SharedPreferences.Editor user = preferences.edit();
                    user.putString("user_id", user_id);
                    user.putString("username", userNameEntered);
                    user.apply();
                    logIn(user_id);
                }
                else {
                    Toast.makeText(this, "Username is invalid, please enter a new username", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.i("username empty", userNameEntered);
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }
    }

    private void logIn(String user_id){
        Intent intent = new Intent(MainActivity.this, UserLeagues.class);
        intent.putExtra("nflState", nflState);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    private void getNFLState(){
        NFLState.nflStateThread thread = new NFLState.nflStateThread();
        Thread t = new Thread(thread);
        t.start();
        try {
            t.join();
            nflState = thread.getNflState();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}