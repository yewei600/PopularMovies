package com.ericwei.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ericwei.popularmovies.data.MovieContract;

import org.json.JSONException;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieCardClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MOVIE_DATA = "movie_data";

    private ActionBar mActionBar;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private String mSortType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mActionBar = getSupportActionBar();

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        setupSharedPreferences();

        if (mSortType.equals(getString(R.string.pref_sort_favorites_value))) {
            mActionBar.setTitle("Favorite Movies");
            new FetchFavoriteMoviesTask().execute();
        } else {
            if (isConnected) {
                if (mSortType.equals(getString(R.string.pref_sort_popular_value))) {
                    mActionBar.setTitle("Popular Movies");
                } else {
                    mActionBar.setTitle("Top Rated Movies");
                }
                new FetchMovieInfoTask().execute(mSortType);
            } else {
                Toast.makeText(this, "No internet connectivity! can only show favorite movies.\nChange in settings",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onMovieCardClick(Movie movieDetail) {

        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailMovieActivity.class);
        intentToStartDetailActivity.putExtra(MOVIE_DATA, movieDetail);
        startActivity(intentToStartDetailActivity);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        getMovieSortingPreference(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void getMovieSortingPreference(SharedPreferences sharedPreferences) {
        mSortType = sharedPreferences.getString(getString(R.string.pref_sort_option_key),
                getString(R.string.pref_sort_popular_value));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.pref_sort_option_key))) {
            getMovieSortingPreference(sharedPreferences);
        }
    }

    public class FetchMovieInfoTask extends AsyncTask<String, Void, Movie[]> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            Context context = MainActivity.this;
            dialog = new ProgressDialog(context);  //can't use getApplicationContext()
            dialog.setMessage("Loading Movie Info...");
            dialog.setCancelable(false);
            //http://stackoverflow.com/questions/7811993/error-binderproxy45d459c0-is-not-valid-is-your-activity-running
            if (!((Activity) context).isFinishing()) {
                dialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            URL movieQueryUrl = NetworkUtils.buildUrl(strings[0]);
            Movie[] jsonMovieResponses = null;

            try {
                jsonMovieResponses = NetworkUtils.getMovieDataFromHttpUrl(movieQueryUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonMovieResponses;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
                Log.d(TAG, "the movie array set to the adapter is of length " + movies.length);
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(movies);
        }
    }

    public class FetchFavoriteMoviesTask extends AsyncTask<Object, Object, Cursor> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            Context context = MainActivity.this;
            dialog = new ProgressDialog(context);  //can't use getApplicationContext()
            dialog.setMessage("Loading Movie Info...");
            dialog.setCancelable(false);
            //http://stackoverflow.com/questions/7811993/error-binderproxy45d459c0-is-not-valid-is-your-activity-running
            if (!((Activity) context).isFinishing()) {
                dialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Object... voids) {
            return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    MovieContract.MovieEntry.COLUMN_ID);
        }

        @Override
        protected void onPostExecute(Cursor moviesCursor) {
            int numMovies = moviesCursor.getCount();

            if (numMovies > 0) {
                Movie[] movies = new Movie[numMovies];
                moviesCursor.moveToFirst();

                for (int i = 0; i < numMovies; i++) {
                    movies[i] = new Movie(moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                            moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)),
                            moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)),
                            moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)),
                            moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                            moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)));

                    moviesCursor.moveToNext();
                }
                mMovieAdapter.setMovieData(movies);
            } else {
                Toast.makeText(getApplicationContext(), "You have no favorite movies!",
                        Toast.LENGTH_LONG).show();
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(moviesCursor);
        }
    }
}