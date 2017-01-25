package com.ericwei.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieCardClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private Toast mToast;
    private int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onMovieCardClick() {
        if (mToast != null) mToast.cancel();

        mToast = Toast.makeText(this, "CLICKED CARD!!! " + String.valueOf(counter), Toast.LENGTH_LONG);
        mToast.show();
        counter++;

        Intent intentToStartDetailActivity = new Intent(this,DetailMovieActivity.class);
        
    }

}
