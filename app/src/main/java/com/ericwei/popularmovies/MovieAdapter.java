package com.ericwei.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ericwei on 2017-01-24.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private Movie[] mMovieData;

    private final MovieCardClickListener mOnClickListener;

    public interface MovieCardClickListener {
        void onMovieCardClick(Movie movieDetail);
    }

    public MovieAdapter(MovieCardClickListener listener) {
        mOnClickListener = listener;
    }


    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_card, parent, shouldAttachToParentImmediately);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {

        holder.mMoviePosterThumbnail.setImageResource(R.mipmap.ic_launcher);
        //holder.movieInfo.setText("YO!!!! please show on the CardView!!!!?");
        //holder.bind();
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public void setMovieData(Movie[] movieData) {
        mMovieData = movieData;
        //WHAT IS THIS FOR?????====================================================================================================================================
        notifyDataSetChanged();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mMoviePosterThumbnail;
        //public TextView movieInfo;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMoviePosterThumbnail = (ImageView) itemView.findViewById(R.id.iv_movie_poster_thumbnail);
            //movieInfo = (TextView) itemView.findViewById(R.id.tv_card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onMovieCardClick(mMovieData[0]);
        }

//        void bind() {
//            mMoviePosterThumbnail.setImageResource(R.mipmap.ic_launcher);
//        }

    }
}
