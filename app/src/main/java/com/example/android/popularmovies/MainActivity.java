package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    final static String SORT_POPULAR = "popular";
    final static String SORT_TOP_RATED = "top_rated";
    final static String MOVIE_TITLE = "original_title";
    final static String MOVIE_POSTER = "movie_poster";
    final static String MOVIE_OVERVIEW = "overview";
    final static String MOVIE_USER_RATING = "user_rating";
    final static String MOVIE_RELEASE_DATE = "release_date";
    public MovieData[] mMoviesData;
    public ArrayList<MovieData> mMovieDataList;
    public MovieAdapter mAdapter;
    GridView mGridView;
    TextView mErrorDisplayMessage;
    ProgressBar mLoadingIndicator;
    String sort = SORT_POPULAR;
    Parcelable state = null;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || !savedInstanceState.containsKey("sort")) {
            sort = SORT_POPULAR;

        } else {
            sort = savedInstanceState.getString("sort");
        }

        setContentView(R.layout.activity_main);
        mGridView = (GridView) findViewById(R.id.gridView);
        mErrorDisplayMessage = (TextView) findViewById(R.id.error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        makeMovieURL(sort);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        state = savedInstanceState.getParcelable("currentState");
        index = savedInstanceState.getInt("gridPos");
        if (state != null) {
            mGridView.requestFocus();
            mGridView.onRestoreInstanceState(state);
            mGridView.setSelection(index);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("sort", sort);
        state = mGridView.onSaveInstanceState();
        outState.putParcelable("currentState", state);
        int index = mGridView.getFirstVisiblePosition();
        outState.putInt("gridPos",index);
        super.onSaveInstanceState(outState);
    }

    /**
     * Creates a URL and executes the Async Task.
     * @param sorting This defines the query being run
     */
    private void makeMovieURL(String sorting) {
        URL movieDBUrl = NetworkUtils.buildUrl(sorting);
        new MovieDbFetchTask().execute(movieDBUrl);
    }

    /**
     * Makes GridView Visible
     * Makes error message invisible
     */
    private void showMoviePosters() {
        mGridView.setVisibility(View.VISIBLE);
        mErrorDisplayMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * Makes GridView Invisible
     * Makes error message Visible
     */
    private void showErrorMessage() {
        mGridView.setVisibility(View.INVISIBLE);
        mErrorDisplayMessage.setVisibility(View.VISIBLE);
    }

    /**
     * This method is called after {@link MovieDbFetchTask#onPostExecute(String)}. This parses the JSON String and creates data.
     * @param responseFromMovieDb this is JSON string.
     * @return {@link MovieData}
     */
    private MovieData[] SetMovieData(String responseFromMovieDb) {
        MovieData[] movies = null;
        try {
            JSONObject jsonObject = new JSONObject(responseFromMovieDb);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            movies = new MovieData[jsonArray.length()];
            for (int i = 0; i < movies.length; i++) {
                // MovieData movieData = new MovieData();

                JSONObject result = jsonArray.getJSONObject(i);
                String title = result.optString("title");
                String poster = result.optString("poster_path");
                String overview = result.optString("overview");
                String userRating = result.optString("vote_average");
                String releaseDate = result.optString("release_date");
                MovieData movieData = new MovieData(title, poster, overview, userRating, releaseDate);
                movies[i] = movieData;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.sort_pop:
                sort = SORT_POPULAR;
                makeMovieURL(sort);
                break;
            case R.id.sort_rate:
                sort = SORT_TOP_RATED;
                makeMovieURL(sort);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    public class MovieDbFetchTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String responseFromMovieDb = null;
            try {
                responseFromMovieDb = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseFromMovieDb;
        }

        @Override
        protected void onPostExecute(String responseFromMovieDb) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (responseFromMovieDb != null && responseFromMovieDb != "") {
                showMoviePosters();
                mMoviesData = SetMovieData(responseFromMovieDb);
                mMovieDataList = new ArrayList<>(Arrays.asList(mMoviesData));
                //mMovieDataList = Arrays.asList(mMoviesData);
                mAdapter = new MovieAdapter(MainActivity.this, mMovieDataList);
                mAdapter.notifyDataSetChanged();
                mGridView.setAdapter(mAdapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(MOVIE_TITLE, mMoviesData[i].getOriginalTitle());
                        intent.putExtra(MOVIE_POSTER, mMoviesData[i].getPosterPath());
                        intent.putExtra(MOVIE_OVERVIEW, mMoviesData[i].getOverview());
                        intent.putExtra(MOVIE_USER_RATING, mMoviesData[i].getUserRating());
                        intent.putExtra(MOVIE_RELEASE_DATE, mMoviesData[i].getReleaseDate());
                        startActivity(intent);
                    }
                });
            } else {
                showErrorMessage();
            }
        }
    }
}
