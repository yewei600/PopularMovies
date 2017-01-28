package com.ericwei.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();

    private Movie mMovie;
    private String mTitle;
    private String mReleaseDate;
    //movie poster
    private String mAverage;
    private String mOverview;

    private TextView mTitleDisplay;
    private TextView mReleaseDateDisplay;
    private TextView mAverageDisplay;
    private TextView mOverviewDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "detail activity LUANCHED!!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.MOVIE_DATA)) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_DATA);
        }


        Log.d(TAG, "movie is not NULL, and the tile is " + mMovie.getPosterPath());

        mTitleDisplay = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseDateDisplay = (TextView) findViewById(R.id.tv_release_date);
        mAverageDisplay = (TextView) findViewById(R.id.tv_average);
        mOverviewDisplay = (TextView) findViewById(R.id.tv_overview);

        mTitleDisplay.setText("Title: " + mMovie.getOriginalTitle());
        mReleaseDateDisplay.setText("Release Date: " + mMovie.getReleaseDate());
        mAverageDisplay.setText("Average Rating: " + mMovie.getVoteAverage());
        mOverviewDisplay.setText("Overview: " + mMovie.getOverview());

    }
}
