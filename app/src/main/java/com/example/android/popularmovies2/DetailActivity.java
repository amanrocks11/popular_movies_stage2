package com.example.android.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i=getIntent();
        String fragtolaunch=i.getExtras().getString("FRAGMENT");
        if(fragtolaunch.equals("Movie")) {
            getFragmentManager().beginTransaction().replace(R.id.detail_frag,new MovieDetailFrag()).commit();
        }
        else
        {
            getFragmentManager().beginTransaction().replace(R.id.detail_frag,new FavoriteDetailFragment()).commit();
        }
    }



}