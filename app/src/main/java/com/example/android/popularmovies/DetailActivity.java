package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailActivity extends AppCompatActivity {
    TextView mMovieTitle;
    ImageView mMoviePoster;
TextView mReleaseDate;
    TextView mUserRating;
    TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        mReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        mUserRating = (TextView) findViewById(R.id.movie_user_rating);
        mOverview = (TextView) findViewById(R.id.movie_overview);

        Intent intent = getIntent();
        mMovieTitle.setText(intent.getStringExtra(MainActivity.MOVIE_TITLE));
        String imageURL="http://image.tmdb.org/t/p/w500"+intent.getStringExtra(MainActivity.MOVIE_POSTER);
        Picasso.with(this).load(imageURL).into(mMoviePoster);
        Pattern pattern = Pattern.compile("(\\d{4})");
        Matcher matcher = pattern.matcher(intent.getStringExtra(MainActivity.MOVIE_RELEASE_DATE));
        int year = 0;
        if (matcher.find()) {
            year = Integer.parseInt(matcher.group(1));
        }
        mReleaseDate.setText("" + year);
        mUserRating.setText(intent.getStringExtra(MainActivity.MOVIE_USER_RATING)+"/10");
        mOverview.setText(intent.getStringExtra(MainActivity.MOVIE_OVERVIEW));
    }
}
