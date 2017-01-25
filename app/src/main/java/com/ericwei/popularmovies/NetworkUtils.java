package com.ericwei.popularmovies;

import java.io.IOException;
import java.net.URL;

/**
 * Created by ericwei on 2017-01-24.
 * <p>
 * 1. The base URL will look like: http://image.tmdb.org/t/p/.
 * 2. Then you will need a ‘size’, which will be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original". For most phones we recommend using “w185”.
 * 3. And finally the poster path returned by the query, in this case “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”
 * <p>
 * USE okhttp API?
 */

public class NetworkUtils {

    final static String TMDB_BASE_URL =
            "http://image.tmdb.org/t/p/";
    //final static String

    public static URL buildURL(String str) {

        return null;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        return null;
    }


}
