package edu.neu.cs5520sp22.fantasyleaguecompanion.standings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;

public class StandingHolder extends RecyclerView.ViewHolder {
    ImageView logo;
    TextView name;
    TextView wins;
    TextView losses;
    TextView ties;
    TextView ppts;
    TextView pts;
    TextView pts_against;

    public StandingHolder(@NonNull View itemView) {
        super(itemView);
        logo = itemView.findViewById(R.id.standing_image);
        name = itemView.findViewById(R.id.standing_name);
        wins = itemView.findViewById(R.id.standing_wins);
        losses = itemView.findViewById(R.id.standing_losses);
        ties = itemView.findViewById(R.id.standing_ties);
        ppts = itemView.findViewById(R.id.standing_ppts);
        pts = itemView.findViewById(R.id.standing_pts);
        pts_against = itemView.findViewById(R.id.standing_pts_against);
    }
}
