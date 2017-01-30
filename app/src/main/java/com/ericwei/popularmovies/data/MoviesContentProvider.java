package com.ericwei.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ericwei.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by ericwei on 2017-01-28.
 */

public class MoviesContentProvider extends ContentProvider {

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, );
        return uriMatcher;
    }

    private MoviesDBHelper mMoviesDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDBHelper = new MoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
