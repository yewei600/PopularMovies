package com.ericwei.popularmovies;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ericwei on 2017-01-24.
 * <p>
 * Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.
 */


public class DetailMovieActivity extends AppCompatActivity {

    private String mTitle;
    private String mReleaseDate;
    //movie poster
    private String mAverage;
    private String mPlotSynopsis;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_movie_detail);


    }


}
