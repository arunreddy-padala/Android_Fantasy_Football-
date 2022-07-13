package edu.neu.cs5520sp22.fantasyleaguecompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import edu.neu.cs5520sp22.fantasyleaguecompanion.api.SleeperApiHelper;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.player.PlayerRepository;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setTitle("Settings");
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            setRosterReload();
            setLastReload();
            setOnUserNameChange();
        }

        private void setRosterReload(){
            Preference reload_roster = getPreferenceManager().findPreference("reload_players");
            reload_roster.setOnPreferenceClickListener(preference -> {
                new Thread(() -> {
                    PlayerRepository playerRepository = new PlayerRepository(getActivity().getApplication());
                    SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SleeperApiHelper.reloadPlayers(playerRepository, p);
                    setLastReload();
                }).start();
                return true;
            });
        }

        private void setLastReload(){
            Preference last_reload = getPreferenceManager().findPreference("last_reload");
            Long t = PreferenceManager.getDefaultSharedPreferences(getActivity()).getLong("player_datetime",0);
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
            last_reload.setSummary(formatter.format(dateTime));
        }

        private void setOnUserNameChange(){
            Preference username = getPreferenceManager().findPreference("username");
            username.setOnPreferenceChangeListener((preference, newValue) -> {
                if(!((EditTextPreference) preference).getText().equals(newValue)) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Are you sure you want to change the User Name")
                            .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                                try {
                                    String userId = SleeperApiHelper.getUserId((String) newValue);
                                    if(userId != null) {
                                        ((EditTextPreference) preference).setText((String) newValue);
                                        SharedPreferences.Editor editor = preference.getPreferenceManager().getSharedPreferences().edit();
                                        editor.putString("user_id", userId);
                                        editor.apply();
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "User Name is invalid please update User Name", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                    return false;
                }
                return true;
            });
        }
    }
}