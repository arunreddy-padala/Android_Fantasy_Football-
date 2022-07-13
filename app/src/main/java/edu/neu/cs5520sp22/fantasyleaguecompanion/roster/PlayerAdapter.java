package edu.neu.cs5520sp22.fantasyleaguecompanion.roster;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.Utilities;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerHolder> {
    private final ArrayList<PlayerRow> players;

    public PlayerAdapter(ArrayList<PlayerRow> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_card, parent, false);
        return new PlayerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
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
        PlayerRow player = players.get(position);
        String temp = player.getDisplayName();
        if(!TextUtils.isEmpty(player.getPosition())){
            temp = temp + " " + player.getPosition();
        }
        if(!TextUtils.isEmpty(player.getTeam())){
            temp = temp + " - " + player.getTeam();
        }
        holder.player_name.setText(temp);
        holder.rosterSpot.setText(player.getRosterSpot());
        holder.image.setImageBitmap(player.getImage());
        holder.rosterSpot.setBackgroundResource(R.drawable.position_background);
        if (Utilities.isRosterPosition(player.getRosterSpot())) {
            holder.rosterSpot.setTextColor(Color.BLACK);
            Drawable background = holder.rosterSpot.getBackground();
            ((GradientDrawable) background).setColor(Color.parseColor(Utilities.RosterColor.valueOf(player.getRosterSpot()).color));
        }
        else if (player.getRosterSpot().equals("IDP") || player.getRosterSpot().equals("TX") || player.getRosterSpot().equals("IR")){
            Drawable background = holder.rosterSpot.getBackground();
            ((GradientDrawable) background).setColor(Color.BLACK);
            holder.rosterSpot.setTextColor(Color.WHITE);
        }
        else{
            switch (player.getRosterSpot()) {
                case "WRT":
                    holder.rosterSpot.setTextColor(Color.BLACK);
                    holder.rosterSpot.setBackgroundResource(R.drawable.position_flex);
                    break;
                case "WR":
                    holder.rosterSpot.setTextColor(Color.BLACK);
                    holder.rosterSpot.setBackgroundResource(R.drawable.position_wrrb_flex);
                    break;
                case "WT":
                    holder.rosterSpot.setTextColor(Color.BLACK);
                    holder.rosterSpot.setBackgroundResource(R.drawable.position_rec_flex);
                    break;
                case "WR\nTQ":
                    holder.rosterSpot.setTextColor(Color.BLACK);
                    holder.rosterSpot.setBackgroundResource(R.drawable.position_super_flex);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
