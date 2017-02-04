package com.example.android.popularmovies2;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import com.example.android.popularmovies2.data.Movie;
import com.example.android.popularmovies2.data.Movie_Contract;
import com.example.android.popularmovies2.data.MovieDbHelper;


public class FavoriteDetailFragment extends Fragment {


    RequestQueue requestQueue;
    SQLiteOpenHelper mdbh;
    SQLiteDatabase mdb;
    Cursor cursor;

    public FavoriteDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_movie_detail,container,false);
        mdbh=new MovieDbHelper(getActivity());
        mdb=mdbh.getReadableDatabase();
        cursor=mdb.rawQuery("select * from " + Movie_Contract.MovieEntry.TABLE_NAME + " where " + Movie_Contract.MovieEntry.MOVIE_ID + "=" + Movie.posfdf, null);
        cursor.moveToFirst();
        TextView title=(TextView) view.findViewById(R.id.text_Title);
        title.setText(cursor.getString(cursor.getColumnIndex(Movie_Contract.MovieEntry.TITLE)));
        TextView release_year=(TextView) view.findViewById(R.id.release_year);
        release_year.setText(cursor.getString(cursor.getColumnIndex(Movie_Contract.MovieEntry.RELEASE)));
        TextView Rating=(TextView) view.findViewById(R.id.rating);
        Rating.setText(cursor.getString(cursor.getColumnIndex(Movie_Contract.MovieEntry.VOTE)) + "/10");
        ImageView poster = (ImageView) view.findViewById(R.id.movie_poster);
        String image_URL = "http://image.tmdb.org/t/p/w185" + cursor.getString(cursor.getColumnIndex(Movie_Contract.MovieEntry.IMAGE));
        Picasso.with(getActivity()).load(image_URL.trim()).into(poster);
        TextView plotSynop=(TextView) view.findViewById(R.id.plot_synop);
        plotSynop.setText(cursor.getString(cursor.getColumnIndex(Movie_Contract.MovieEntry.PLOT)));
        Button favMov=(Button) view.findViewById(R.id.favoriteMov);
        favMov.setText("Fav");
        makeRequest();
        return view;
    }

    public void makeRequest() {

        String final_URL = cursor.getString(cursor.getColumnIndex(Movie_Contract.MovieEntry.VIDEO));
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, final_URL, null,
                new Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ja = response.getJSONArray("results");

                            Video.videarray=new Video[ja.length()];

                            for (int i=0;i<ja.length();i++)
                            {
                                JSONObject job=ja.getJSONObject(i);
                                Video item=new Video();
                                item.key=job.getString("key");
                                item.name=job.getString("name");
                                item.size=job.getString("size");
                                item.type=job.getString("type");
                                Video.videarray[i]=item;
                            }

                            VideoFragment vf=new VideoFragment();
                            try {
                                vf.vilist = Arrays.asList(Video.videarray);
                                getChildFragmentManager().beginTransaction().replace(R.id.vid_container,vf).commitAllowingStateLoss();

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );

        final_URL=cursor.getString(cursor.getColumnIndex(Movie_Contract.MovieEntry.REVIEW));

        JsonObjectRequest jor2 = new JsonObjectRequest(Request.Method.GET, final_URL, null,
                new Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ja = response.getJSONArray("results");

                            Review.review=new Review[ja.length()];

                            for (int i=0;i<ja.length();i++)
                            {
                                JSONObject job=ja.getJSONObject(i);
                                Review review1=new Review();
                                review1.author=job.getString("author");
                                review1.content=job.getString("content");
                                Review.review[i]=review1;
                            }

                            ReviewFragment rf=new ReviewFragment();
                            rf.relist=Arrays.asList(Review.review);
                            if(rf!=null) {
                                try{
                                    getChildFragmentManager().beginTransaction().replace(R.id.Review_container, rf).commitAllowingStateLoss();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", String.valueOf(error));

                    }
                }
        );


        requestQueue.add(jor);
        requestQueue.add(jor2);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(cursor!=null)
            cursor.close();
        if(mdb!=null)
            mdb.close();
        if(mdbh!=null)
            mdbh.close();
        if(requestQueue!=null)
            requestQueue.cancelAll(getActivity());
    }


}
