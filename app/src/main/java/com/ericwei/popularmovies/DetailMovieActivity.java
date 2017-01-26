package com.ericwei.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();

    private Movie mMovie;
    private String mTitle;
    private String mReleaseDate;
    //movie poster
    private String mAverage;
    private String mPlotSynopsis;

    private TextView mTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "detail activity LUANCHED!!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.MOVIE_DATA)) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_DATA);
        }


        Log.d(TAG, "movie is not NULL, and the tile is " + mMovie.getOriginalTitle());

        mTV = (TextView) findViewById(R.id.tv_single_movie_info);

        mTV.setText(mMovie.getOriginalTitle() + " " + mMovie.getPosterPath() + " " + mMovie.getOverview());
    }
}
