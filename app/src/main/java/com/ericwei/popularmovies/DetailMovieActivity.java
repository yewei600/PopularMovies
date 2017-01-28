package com.ericwei.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();

    private Movie mMovie;

    private TextView mTitleDisplay;
    private TextView mReleaseDateDisplay;
    private TextView mAverageDisplay;
    private TextView mOverviewDisplay;
    private ImageView mPosterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.MOVIE_DATA)) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_DATA);
        }

        mTitleDisplay = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseDateDisplay = (TextView) findViewById(R.id.tv_release_date);
        mAverageDisplay = (TextView) findViewById(R.id.tv_average);
        mOverviewDisplay = (TextView) findViewById(R.id.tv_overview);
        mPosterImage = (ImageView) findViewById(R.id.iv_movie_poster);

        mTitleDisplay.setText("Title: " + mMovie.getOriginalTitle());
        mReleaseDateDisplay.setText("Release Date: " + mMovie.getReleaseDate());
        mAverageDisplay.setText("Average Rating: " + mMovie.getVoteAverage());
        mOverviewDisplay.setText("Overview: " + mMovie.getOverview());

        String thumbnailUrl = "http://image.tmdb.org/t/p/w342" + mMovie.getPosterPath();
        Picasso.with(this).load(thumbnailUrl).into(mPosterImage);
    }
}
