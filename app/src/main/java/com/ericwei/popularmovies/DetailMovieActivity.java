package com.ericwei.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();

    private Movie mMovie;

    private TextView mTitleDisplay;
    private TextView mReleaseDateDisplay;
    private TextView mAverageDisplay;
    private TextView mOverviewDisplay;
    private ImageView mPosterImage;

    private ActionBar mActionbar;
    private Button mTestButton;

    private String[] youtubeTrailerIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.MOVIE_DATA)) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_DATA);
        }

        mActionbar = getSupportActionBar();
        mActionbar.setTitle("Movie Detail");

        mTitleDisplay = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseDateDisplay = (TextView) findViewById(R.id.tv_release_date);
        mAverageDisplay = (TextView) findViewById(R.id.tv_average);
        mOverviewDisplay = (TextView) findViewById(R.id.tv_overview);
        mPosterImage = (ImageView) findViewById(R.id.iv_movie_poster);

        mTestButton = (Button) findViewById(R.id.testButton);

        mTitleDisplay.setText("Title: " + mMovie.getOriginalTitle());
        mReleaseDateDisplay.setText("Release Date: " + mMovie.getReleaseDate());
        mAverageDisplay.setText("Average Rating: " + mMovie.getVoteAverage());
        Log.d(TAG, "the movie ID is " + mMovie.getId());


        String thumbnailUrl = "http://image.tmdb.org/t/p/w342" + mMovie.getPosterPath();
        Picasso.with(this).load(thumbnailUrl).into(mPosterImage);

        new FetchTrailersAndReviewsTask().execute();

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (youtubeTrailerIds != null) {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + youtubeTrailerIds[0]));
                    startActivity(intent1);
                }
            }
        });
    }


    public class FetchTrailersAndReviewsTask extends AsyncTask<Void, Void, HashMap<String, String>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DetailMovieActivity.this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {
            URL[] urls = NetworkUtils.getTrailersAndReviewsUrls(mMovie.getId());
            HashMap<String, String> movieReviews = null;
            try {
                youtubeTrailerIds = NetworkUtils.getMovieTrailerIds(urls[0]);
                movieReviews = NetworkUtils.getMovieReviews(urls[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieReviews;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> movieReviewsHashMap) {
            if (movieReviewsHashMap != null) {
                List authorList = new ArrayList(movieReviewsHashMap.keySet());

                mAverageDisplay.setText("the review list is length = " + movieReviewsHashMap.size() + "\n");

                for (int i = 0; i < youtubeTrailerIds.length; i++) {
                    mAverageDisplay.append(youtubeTrailerIds[i] + "\n\n");
                }
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(movieReviewsHashMap);
        }
    }
}
