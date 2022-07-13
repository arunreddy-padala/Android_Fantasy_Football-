package edu.neu.cs5520sp22.fantasyleaguecompanion.league;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;
import edu.neu.cs5520sp22.fantasyleaguecompanion.utils.ItemClickListener;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueUserHolder> {

    public ArrayList<LeagueItem> users;
    private ItemClickListener listener;

    public LeagueAdapter(ArrayList<LeagueItem> users, ItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LeagueUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.league_card, parent, false);
        Log.d("create view", "view create");
        return new LeagueUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueUserHolder holder, int position) {
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
        LeagueItem user = users.get(position);
        Log.d("view", user.displayName);
        holder.leagueUserImage.setImageBitmap(user.image);
        holder.leagueUserName.setText(user.displayName);
        holder.setOnClick(listener, user.id, user.displayName);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
