package com.ericwei.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ericwei on 2017-01-28.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.ericwei.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_MOVIES="movies";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";


    }
}
