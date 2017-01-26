package com.ericwei.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ericwei on 2017-01-24.
 * <p>
 * guide to using Parcelable:
 * https://guides.codepath.com/android/using-parcelable
 */

public class Movie implements Parcelable {

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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalTitle);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeString(this.voteAverage);
        dest.writeString(this.overview);
    }

    protected Movie(Parcel in) {
        this.originalTitle = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.voteAverage = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
