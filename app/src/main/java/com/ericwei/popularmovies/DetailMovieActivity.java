package com.ericwei.popularmovies;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ericwei.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ericwei.popularmovies.data.MovieContract.MovieEntry;

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

    private ListView mTrailersListView;
    private TrailersListAdapter mTrailersListAdapter;
    private ExpandableListView mReviewsExpListView;
    private ReviewsExpandableListAdapter mReviewsExpListAdapter;

    private String[] youtubeTrailerIds;
    private boolean isFavorite;

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
        mTrailersListView = (ListView) findViewById(R.id.lv_trailers);
        mReviewsExpListView = (ExpandableListView) findViewById(R.id.elv_reviews);

        mTitleDisplay.setText("Title: " + mMovie.getOriginalTitle());
        mReleaseDateDisplay.setText("Release Date: " + mMovie.getReleaseDate());
        mAverageDisplay.setText("Average Rating: " + mMovie.getVoteAverage());
        mOverviewDisplay.setText(mMovie.getOverview());
        Log.d(TAG, "the movie ID is " + mMovie.getId());
        String thumbnailUrl = "http://image.tmdb.org/t/p/w342" + mMovie.getPosterPath();
        Picasso.with(this).load(thumbnailUrl).error(R.mipmap.ic_launcher).into(mPosterImage);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected) {
            new FetchTrailersAndReviewsTask().execute();
        } else {
            Toast.makeText(this, "No internet connection! Can't show trailers and reviews",
                    Toast.LENGTH_LONG).show();
        }

        if (isMovieInFavoriteDB()) {
            mTestButton.setText("Unfavorite Movie");
        } else {
            mTestButton.setText("Favorite Movie");
        }

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteDatabaseOperation(isFavorite);
            }
        });
    }

    private boolean isMovieInFavoriteDB() {
        Cursor cursor = getContentResolver().query(
                MovieEntry.CONTENT_URI,
                new String[]{MovieEntry.COLUMN_TITLE},
                MovieEntry.COLUMN_ID + "='" + mMovie.getId() + "'",
                null, null);

        if (cursor.getCount() > 0) {
            Log.d(TAG, "This movie is in the database!!!");
            mTestButton.setText("unfavorite!");
            isFavorite = true;
        } else {
            Log.d(TAG, "This movie NOT IN database!");
            isFavorite = false;
        }
        return isFavorite;
    }

    //save a movie to the favorite db
    private void favoriteDatabaseOperation(boolean isInDb) {
        /*
      The titles and ids of the user's favorite movies are stored in a ContentProvider backed by a
      SQLite database. This ContentProvider is updated whenever the user favorites or unfavorites a movie.
         */
        if (isInDb) {
            Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(mMovie.getId()).build();
            int deleted = getContentResolver().delete(uri, null, null);
            if (deleted == 1) {
                Toast.makeText(this, "Deleted from favorites", Toast.LENGTH_LONG).show();
                mTestButton.setText("Favorite Movie");
            }
        } else {
            //insert into database
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getOriginalTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, mMovie.getPosterPath());
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_ID, Integer.parseInt(mMovie.getId()));

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_LONG).show();
                mTestButton.setText("Unfavorite Movie");
            }
        }
    }

    private String[] generateListViewData(int size) {
        String[] data = new String[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = "Trailer " + String.valueOf(i + 1);
        }
        return data;
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1,
                        generateListViewData(youtubeTrailerIds.length));
                mTrailersListView.setAdapter(adapter);
                mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + youtubeTrailerIds[i]));
                        startActivity(intent1);
                    }
                });

                mReviewsExpListAdapter = new ReviewsExpandableListAdapter(getApplicationContext(),
                        movieReviewsHashMap, authorList);
                mReviewsExpListView.setAdapter(mReviewsExpListAdapter);
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(movieReviewsHashMap);
        }
    }

}
