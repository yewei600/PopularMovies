package com.ericwei.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

    public static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/popular?api_key=506f49d96a360fcfd9fd74de6d1dbe67";
    //  /movie/popular?api_key=
    //"http://image.tmdb.org/t/p/";
    private static final String POPULAR_SEARCH = "/movie/popular";
    private static final String TOP_RATED_SEARCH = "/movie/top_rated";
    private static OkHttpClient client = new OkHttpClient();


    public static URL buildURL() {
//        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
//                .appendQueryParameter(POPULAR_SEARCH,)
        URL url = null;
        try {
            url = new URL(TMDB_BASE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static Movie[] getResponseFromHttpUrl(URL url) throws IOException {

        Request request = new Request.Builder()
                .url(buildURL()).build();

        Movie[] movieItems = null;
        try {
            Log.d(TAG, "ABOUT TO get network data!!!");
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.d(TAG, "the JSON operation is not successful. code " + response.code());
            }
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            movieItems = new Movie[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                Log.d(TAG, "hey! I'm assembling the movie array! i= " + String.valueOf(i));
                //String originalTitle, String releaseDate, String posterPath, String voteAverage, String overview
                movieItems[i] = new Movie(object.getString("original_title"),
                        object.getString("release_date"),
                        object.getString("poster_path"),
                        object.getString("vote_average"),
                        object.getString("overview"));
            }
            response.body().close();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieItems;
    }
}
