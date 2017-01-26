package com.ericwei.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieCardClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MOVIE_DATA = "movie_data";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        new FetchMovieInfoTask().execute("hello");
    }

    @Override
    public void onMovieCardClick(Movie movieDetail) {

        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailMovieActivity.class);
        intentToStartDetailActivity.putExtra(MOVIE_DATA, movieDetail);
        Log.d(TAG, "startign Detailed activity!!! and passing movie titled " + movieDetail.getOriginalTitle());
        startActivity(intentToStartDetailActivity);
    }


    public class FetchMovieInfoTask extends AsyncTask<String, Void, Movie[]> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);  //can't use getApplicationContext()
            dialog.setMessage("Loading Movie Info...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            URL movieQueryUrl = NetworkUtils.buildURL();
            Movie[] jsonMovieResponses = null;

            try {
                jsonMovieResponses = NetworkUtils.getResponseFromHttpUrl(movieQueryUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonMovieResponses;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            Log.d(TAG, "onPostExecute of MAINACTIVITY");

            if (movies != null) {
                Log.d(TAG, "so movies[] ISN'T NULL!!!!");
                mMovieAdapter.setMovieData(movies);
            }
            dialog.hide();
            super.onPostExecute(movies);
        }
    }

}
