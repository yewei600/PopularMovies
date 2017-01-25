package com.ericwei.popularmovies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    private static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/popular?api_key=506f49d96a360fcfd9fd74de6d1dbe67";
    //  /movie/popular?api_key=
    //"http://image.tmdb.org/t/p/";
    private static final String POPULAR_SEARCH = "/movie/popular";
    private static final String TOP_RATED_SEARCH = "/movie/top_rated";


    public static URL buildURL() throws MalformedURLException {
//        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
//                .appendQueryParameter(POPULAR_SEARCH,)
        URL url = new URL(TMDB_BASE_URL);
        return url;
    }

    public static void getResponseFromHttpUrl(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(buildURL()).build();

        try {
            Response response = client.newCall(request).execute();
            JSONArray array = new JSONArray(response.body().string());

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                //String originalTitle, String releaseDate, String posterPath, String voteAverage, String overview
                Movie movieItem = new Movie(object.getString("original_title"),
                        object.getString("release_date"),
                        object.getString("poster_path"),
                        object.getString("vote_average"),
                        object.getString("overview"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
