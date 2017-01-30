package com.ericwei.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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
 * <p>
 * <p>
 * the videos of a movie
 * http://api.themoviedb.org/3/movie/157336/videos?api_key=506f49d96a360fcfd9fd74de6d1dbe67
 * <p>
 * the review of a movie
 * http://api.themoviedb.org/3/movie/157336/reviews?api_key=506f49d96a360fcfd9fd74de6d1dbe67
 */

public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();

    private static OkHttpClient client = new OkHttpClient();


    public static URL buildUrl(String sortType) {

        URL url = null;
        try {
            if (sortType.equals("top rated")) {
                url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key="
                        + BuildConfig.TMDB_API_KEY);
            } else if (sortType.equals("popular")) {
                url = new URL("http://api.themoviedb.org/3/movie/popular?api_key="
                        + BuildConfig.TMDB_API_KEY);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static JSONArray getJsonArrayFromHttpUrl(URL url) {
        Request request = new Request.Builder()
                .url(url).build();
        JSONArray jsonArray = null;

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.d(TAG, "the JSON operation is not successful. code " + response.code());
            }
            JSONObject jsonObject = new JSONObject(response.body().string());
            jsonArray = jsonObject.getJSONArray("results");

            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static Movie[] getMovieDataFromHttpUrl(URL url) throws JSONException {
        JSONArray jsonArray = getJsonArrayFromHttpUrl(url);

        Movie[] movieItems = new Movie[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            //String originalTitle, String releaseDate, String posterPath, String voteAverage, String overview
            movieItems[i] = new Movie(object.getString("original_title"),
                    object.getString("release_date"),
                    object.getString("poster_path"),
                    object.getString("vote_average"),
                    object.getString("overview"),
                    object.getString("id"));
        }
        return movieItems;
    }

    public static URL[] getTrailersAndReviewsUrls(String id) {
        URL[] urls = new URL[2];
        try {
            if (id.length() > 0) {
                urls[0] = new URL("http://api.themoviedb.org/3/movie/" + id
                        + "/videos?api_key="
                        + BuildConfig.TMDB_API_KEY);
                urls[1] = new URL("http://api.themoviedb.org/3/movie/" + id
                        + "/reviews?api_key="
                        + BuildConfig.TMDB_API_KEY);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urls;
    }

    public static HashMap<String, String> getMovieReviews(URL url) throws JSONException {
        HashMap<String, String> reviews = new HashMap<>();

        JSONArray jsonArray = getJsonArrayFromHttpUrl(url);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            reviews.put(object.getString("author"), object.getString("content"));
        }
        return reviews;
    }

    public static String[] getMovieTrailerIds(URL url) throws JSONException {
        JSONArray jsonArray = getJsonArrayFromHttpUrl(url);

        String[] trailerIds = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            trailerIds[i] = object.getString("key");
        }
        return trailerIds;
    }
}
