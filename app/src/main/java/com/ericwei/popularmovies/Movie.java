package com.ericwei.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ericwei on 2017-01-24.
 */

public class Movie implements Parcelable{

    private String originalTitle;
    private String releaseDate;
    private String posterPath;
    private String voteAverage;
    private String overview;

    public Movie(String originalTitle, String releaseDate, String posterPath, String voteAverage, String overview) {
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(voteAverage);
        parcel.writeString(overview);
    }
}
