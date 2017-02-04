package com.example.android.popularmovies2;

/**
 * Created by aman on 04-02-2017.
 */

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ContentValues;
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
import android.widget.Toast;

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

import java.lang.reflect.Field;
import java.util.Arrays;

import com.example.android.popularmovies2.data.Movie;
import com.example.android.popularmovies2.data.Movie_Contract;
import com.example.android.popularmovies2.data.MovieDbHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFrag extends Fragment implements View.OnClickListener {

    public MovieDetailFrag() {
        // Required empty public constructor
    }
    int position= Movie.position_fordetailfrag;
    RequestQueue requestQueue;

    SQLiteOpenHelper movieDB;

    Movie movie;
    String BASE_URL = "http://api.themoviedb.org/3/movie/";
    String final_URL;
    String rel_year;
    Button favMov;
    SQLiteDatabase db;
    ContentValues input;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
//        ButterKnife.bind(this, view);

        movieDB = new MovieDbHelper(getActivity());
        db = movieDB.getWritableDatabase();
        movie = Movie.movieArray[position];
        makeRequest();

        TextView title=(TextView) view.findViewById(R.id.text_Title);
        title.setText(movie.getmmTitle());

        ImageView poster = (ImageView) view.findViewById(R.id.movie_poster);
        String image_URL = "http://image.tmdb.org/t/p/w185" + movie.getImgPath();
        Picasso.with(getActivity()).load(image_URL.trim()).into(poster);

        TextView release_year=(TextView) view.findViewById(R.id.release_year);
        String[] date = movie.getmRelDate().split("-");
        rel_year=date[0];
        release_year.setText(date[0]);

        TextView Rating=(TextView) view.findViewById(R.id.rating);
        Rating.setText(movie.getVoteAvg() + "/10");

        TextView plotSynop=(TextView) view.findViewById(R.id.plot_synop);
        plotSynop.setText(movie.getPlotsynop());

        favMov=(Button) view.findViewById(R.id.favoriteMov);
        Cursor c=db.rawQuery("select "+ Movie_Contract.MovieEntry.TITLE+" from "+ Movie_Contract.MovieEntry.TABLE_NAME+" where "+ Movie_Contract.MovieEntry.TITLE+"="+"\""+movie.getmmTitle()+"\"",null);
        if(c.getCount()>0)
        {
            favMov.setText("Fav");
        }
        else {
            favMov.setText("Mark");
            favMov.setOnClickListener(this);
        }
        c.close();

        return view;
    }

    @Override
    public void onDetach() {
        db.close();
        movieDB.close();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        super.onDetach();
    }

    public void makeRequest() {
        String id = movie.get_id() + "/";
        final_URL = BASE_URL + id + "videos?" + Movie.api_key;
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

        final_URL = BASE_URL + id + "reviews?" + Movie.api_key;

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


    @TargetApi(23)
    @Override
    public void onClick(View view) {
        try {

            if(ifTableExists()) {
                input = new ContentValues();
                input.put(Movie_Contract.MovieEntry.TITLE, movie.getmmTitle());
                input.put(Movie_Contract.MovieEntry.RELEASE, rel_year);
                input.put(Movie_Contract.MovieEntry.VOTE, movie.getVoteAvg());
                input.put(Movie_Contract.MovieEntry.PLOT, movie.getPlotsynop());
                input.put(Movie_Contract.MovieEntry.IMAGE, movie.getImgPath());
                input.put(Movie_Contract.MovieEntry.REVIEW, BASE_URL + movie.get_id() + "/reviews?" + Movie.api_key);
                input.put(Movie_Contract.MovieEntry.Position,movie.getposition_fordb());
                input.put(Movie_Contract.MovieEntry.MOVIE_ID, movie.get_id());
                input.put(Movie_Contract.MovieEntry.VIDEO, BASE_URL + movie.get_id() + "/videos?" + Movie.api_key);
                int r_id = (int) db.insert(Movie_Contract.MovieEntry.TABLE_NAME, null, input);
                if (r_id == -1) {
                    Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }
                else {
                    favMov.setText("Fav");
                    favMov.setOnClickListener(null);
                }

            }
            else
            {
                Toast.makeText(getActivity(), "No Table", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e)
        {
            Log.e("Movie_DB_ERROR", String.valueOf(e));
            Toast.makeText(getActivity(),"try again."+String.valueOf(e),Toast.LENGTH_SHORT).show();
        }
    }

    public boolean ifTableExists()
    {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+ Movie_Contract.MovieEntry.TABLE_NAME+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}
