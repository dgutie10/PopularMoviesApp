package com.example.android.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by diegog on 3/14/2017.
 */


public class Movies implements Parcelable{
    private String title;
    private String overview;
    private String posterLocation;
    private String releaseDate;
    private String rating;
    private static  final String baseImageUrl = "https://image.tmdb.org/t/p/w185";
    private int id;

    public Movies(String title, String overview, String posterLocation, int id, String releaseDate, String rating) {
        this.title = title;
        this.overview = overview;
        this.posterLocation = posterLocation;
        this.id = id;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public Movies(Parcel parcel) {
        title = parcel.readString();
        overview= parcel.readString();
        id = parcel.readInt();
        releaseDate = parcel.readString();
        rating = parcel.readString();
        posterLocation = parcel.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterLocation() {
        return baseImageUrl+posterLocation;
    }

    public int getId() {
        return id;
    }

    public String getReleaseDate() {return releaseDate;}

    public String getRating() {return rating;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTitle());
        parcel.writeString(getOverview());
        parcel.writeInt(getId());
        parcel.writeString(getReleaseDate());
        parcel.writeString(getRating());
        parcel.writeString(this.posterLocation);
    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>(){

        @Override
        public Movies createFromParcel(Parcel parcel) {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int i) {
            return new Movies[0];
        }
    };
}

