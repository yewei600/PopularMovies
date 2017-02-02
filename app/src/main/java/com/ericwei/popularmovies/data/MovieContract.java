package com.ericwei.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ericwei on 2017-01-28.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.ericwei.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_ID = "id";

    }
}
