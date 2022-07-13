package edu.neu.cs5520sp22.fantasyleaguecompanion.matchup;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;

public class MatchupHolder extends RecyclerView.ViewHolder {
    public ImageView userimage1;
    public ImageView userimage2;
//    public TextView username1;
//    public TextView username2;
    public TextView teamname1;
    public TextView teamname2;
    public TextView points1;
    public TextView points2;

    public MatchupHolder(@NonNull View itemView){
        super(itemView);
        userimage1 = itemView.findViewById(R.id.userimg1);
        userimage2 = itemView.findViewById(R.id.userimg2);
//        username1 = itemView.findViewById(R.id.username1);
//        username2 = itemView.findViewById(R.id.username2);
        teamname1 = itemView.findViewById(R.id.teamName1);
        teamname2 = itemView.findViewById(R.id.teamName2);
        points1 = itemView.findViewById(R.id.points1);
        points2 = itemView.findViewById(R.id.points2);


    }
}
