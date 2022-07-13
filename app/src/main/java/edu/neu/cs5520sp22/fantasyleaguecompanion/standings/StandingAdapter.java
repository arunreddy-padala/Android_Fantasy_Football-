package edu.neu.cs5520sp22.fantasyleaguecompanion.standings;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ItemClickListener;


public class StandingAdapter extends RecyclerView.Adapter<StandingHolder> {
    private final ArrayList<StandingItem> standingItems;

    public StandingAdapter(ArrayList<StandingItem> standingItems) {
        this.standingItems = standingItems;
    }

    @NonNull
    @Override
    public StandingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.standing, parent, false);
        return new StandingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StandingHolder holder, int position) {
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
        StandingItem standingItem = standingItems.get(position);
        holder.logo.setImageBitmap(standingItem.getImage());
        holder.name.setText(standingItem.getName());
        holder.wins.setText(String.valueOf(standingItem.getWins()));
        holder.losses.setText(String.valueOf(standingItem.getLosses()));
        holder.ties.setText(String.valueOf(standingItem.getTies()));
        holder.ppts.setText(standingItem.getPpts());
        holder.pts.setText(standingItem.getPts());
        holder.pts_against.setText(standingItem.getPts_against());
    }

    @Override
    public int getItemCount() {
        return standingItems.size();
    }
}
