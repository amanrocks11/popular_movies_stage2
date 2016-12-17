package com.example.android.popularmovies;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by aman on 16-12-2016.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    final static String API_KEY = "";
    final static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    final static String PARAM_API = "api_key";

    /**
     * This method here build the URL for the moviedb query. It takes on parameter based on sorting we require
     * @param sortType
     * @return
     */
    public static URL buildUrl(String sortType) {
        Uri builtUri = Uri.parse(BASE_URL+sortType).buildUpon()
                .appendQueryParameter(PARAM_API,API_KEY).build();
        URL url = null;
        try {
            url = new URL (builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method tries to get the response from the server.
     * @param url
     * @return
     * @throws IOException
     */

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in =urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
        }


}
