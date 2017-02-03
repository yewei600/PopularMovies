package com.ericwei.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ericwei on 2017-01-24.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private Movie[] mMovieData;

    private final MovieCardClickListener mOnClickListener;

    private Context mContext;

    public interface MovieCardClickListener {
        void onMovieCardClick(Movie movieDetail);
    }

    public MovieAdapter(Context context, MovieCardClickListener listener) {
        mContext = context;
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
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.length;
    }

    public void setMovieData(Movie[] movieData) {
        mMovieData = movieData;
        //this function updates the number of views after the movie data has been loaded (from 0 to mMovieData.length)
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMoviePosterThumbnail;
        private TextView mMoviePosterText;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMoviePosterThumbnail = (ImageView) itemView.findViewById(R.id.iv_movie_poster_thumbnail);
            mMoviePosterText = (TextView) itemView.findViewById(R.id.tv_poster_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onMovieCardClick(mMovieData[getAdapterPosition()]);
        }

        void bind(final int position) {
            String thumbnailUrl = "http://image.tmdb.org/t/p/w342" + mMovieData[position].getPosterPath();

            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    mMoviePosterText.setText(mMovieData[position].getOriginalTitle());
                    mMoviePosterText.setVisibility(View.VISIBLE);
                }
            });
            Picasso.with(mContext).load(thumbnailUrl).into(mMoviePosterThumbnail);
        }
    }
}
