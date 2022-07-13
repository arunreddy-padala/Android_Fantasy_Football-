package edu.neu.cs5520sp22.fantasyleaguecompanion.roster;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;

public class PlayerHolder extends RecyclerView.ViewHolder {
    public TextView player_name;
    public TextView rosterSpot;
    public ImageView image;

    public PlayerHolder(@NonNull View itemView) {
        super(itemView);
        player_name = itemView.findViewById(R.id.player_name);
        rosterSpot = itemView.findViewById(R.id.position);
        image = itemView.findViewById(R.id.image);
    }
}
