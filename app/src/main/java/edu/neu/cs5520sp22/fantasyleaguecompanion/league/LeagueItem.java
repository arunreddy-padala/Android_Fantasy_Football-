package edu.neu.cs5520sp22.fantasyleaguecompanion.league;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class LeagueItem implements Parcelable {
    final String id;
    final String displayName;
    final Bitmap image;

    public LeagueItem(String id, String displayName, Bitmap image) {
        this.id = id;
        this.displayName = displayName;
        this.image = image;
    }

    protected LeagueItem(Parcel in) {
        id = in.readString();
        displayName = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<LeagueItem> CREATOR = new Creator<LeagueItem>() {
        @Override
        public LeagueItem createFromParcel(Parcel in) {
            return new LeagueItem(in);
        }

        @Override
        public LeagueItem[] newArray(int size) {
            return new LeagueItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(displayName);
        parcel.writeParcelable(image, i);
    }
}