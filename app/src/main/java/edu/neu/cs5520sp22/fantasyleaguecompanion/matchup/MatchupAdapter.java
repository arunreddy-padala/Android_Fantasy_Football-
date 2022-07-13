package edu.neu.cs5520sp22.fantasyleaguecompanion.matchup;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.database.matchups.Matchup;

public class MatchupAdapter extends RecyclerView.Adapter<MatchupHolder> {
    private final ArrayList<MatchupRow> matchups;

    public MatchupAdapter(ArrayList<MatchupRow> matchups) {
        this.matchups = matchups;
    }

    @NonNull
    @Override
    public MatchupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matchup_card,parent,false);
        return new MatchupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchupHolder holder, int position) {
        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#D9D9D9"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#F0F0F0"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        MatchupRow matchup = matchups.get(position);
        //Log.d("Matchup",matchup.get)
        holder.userimage1.setImageBitmap(matchup.getUserimage1());
        holder.userimage2.setImageBitmap(matchup.getUserimage2());
//        Log.d("Matchup","Username 1: "+matchup.getUsername1());
//        Log.d("Matchup","Username 2: "+matchup.getUsername2());
//        holder.username2.setText(matchup.getUsername2());
//        holder.username1.setText(matchup.getUsername1());
        holder.teamname1.setText(matchup.getTeamname1());
        holder.teamname2.setText(matchup.getTeamname2());
        holder.points1.setText(matchup.getPoints1());
        holder.points2.setText(matchup.getPoints2());

    }

    @Override
    public int getItemCount() {
        return matchups.size();
    }
}
