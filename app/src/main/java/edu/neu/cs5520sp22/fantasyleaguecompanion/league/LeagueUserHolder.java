package edu.neu.cs5520sp22.fantasyleaguecompanion.league;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ItemClickListener;

public class LeagueUserHolder extends RecyclerView.ViewHolder {
    TextView leagueUserName;
    ImageView leagueUserImage;
    public LeagueUserHolder(@NonNull View itemView) {
        super(itemView);
        this.leagueUserName = itemView.findViewById(R.id.league_user_name);
        this.leagueUserImage = itemView.findViewById(R.id.league_user_image);
    }

    public void setOnClick(ItemClickListener listener, String id, String name){
        this.itemView.setOnClickListener(view -> listener.onItemClick(id, name));
    }
}