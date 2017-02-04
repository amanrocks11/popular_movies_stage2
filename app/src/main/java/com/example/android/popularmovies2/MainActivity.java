package com.example.android.popularmovies2;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import com.example.android.popularmovies2.data.Movie;
import com.example.android.popularmovies2.data.Movie_Contract;
import com.example.android.popularmovies2.data.MovieDbHelper;
import com.example.android.popularmovies2.utility.UtilityMethods;


public class MainActivity extends ActionBarActivity implements ClickCallback{

    public static String user_pref=null;
    public static String BASE_URL="http://api.themoviedb.org/3/movie/";

    public static String final_URL=null;
    ProgressDialog loader=null;
    RequestQueue requestQueue;
    boolean HAS_VIEW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader=new ProgressDialog(this);
        if(savedInstanceState!=null)
        {
            user_pref=savedInstanceState.getString("USR_PREF");
            if(HAS_VIEW)
                Movie.position_fordetailfrag=savedInstanceState.getInt("POS");
        }

        if(!UtilityMethods.isNetworkAvailable(this))
        {
            AlertDialog.Builder dialogbuilder=new AlertDialog.Builder(this);
            LayoutInflater inflater=this.getLayoutInflater();
            View view=inflater.inflate(R.layout.dialog_layout,null);
            TextView tv=(TextView)view.findViewById(R.id.error_message);
            tv.setText("We have no Internet");
            dialogbuilder.setTitle("Houston! We have a Problem.").setView(view).setNeutralButton("ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog=dialogbuilder.create();
            alertDialog.show();

        }

        if(savedInstanceState==null && HAS_VIEW)
        {
            Movie.position_fordetailfrag=0;
        }

        View view=findViewById(R.id.detail_container);
        if(view!=null) HAS_VIEW=true;
        if(user_pref==null)
        {
            user_pref="popular?";
        }
        if(user_pref!="favorite" && user_pref!=null) {
            if(UtilityMethods.isNetworkAvailable(this))
                loader.setMessage("Please Wait...");
            loader.show();
            makeRequest();
        }
        else
        {
            loader.dismiss();
            loader=null;
            launchFavoritefragment();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("USR_PREF",user_pref);
        if(HAS_VIEW) outState.putInt("POS",Movie.position_fordetailfrag);
        super.onSaveInstanceState(outState);
    }


    public void makeRequest()
    {
        final_URL=BASE_URL+user_pref+Movie.api_key;
        final_URL.trim();
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, final_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray ja = response.getJSONArray("results");

                            Movie.movieArray=new Movie[ja.length()];

                            for(int i=0; i < ja.length(); i++){

                                Movie movie=new Movie();
                                JSONObject jsonObject = ja.getJSONObject(i);
                                movie.setposition_fordb(i);
                                String posterPath=jsonObject.getString("poster_path");
                                String plotSynop=jsonObject.getString("overview");
                                String relDate=jsonObject.getString("release_date");
                                String _id=jsonObject.getString("id");
                                String title=jsonObject.getString("original_title");
                                String vote_avg=jsonObject.getString("vote_average");

                                movie.set_id(_id);
                                movie.setImgPath(posterPath);
                                movie.setmmTitle(title);
                                movie.setPlotsynop(plotSynop);
                                movie.setmRelDate(relDate);
                                movie.setVoteAvg(vote_avg);

                                Movie.movieArray[i]=movie;
                            }

                            if(loader!=null)
                                loader.dismiss();

                            MovieGridFrag mgf=new MovieGridFrag();
                            mgf.movieList= Arrays.asList(Movie.movieArray);
                            FragmentTransaction ft=getFragmentManager().beginTransaction();
                            ft.replace(R.id.list_container,mgf);
                            try{
                                ft.commitAllowingStateLoss();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            if(HAS_VIEW)
                                defaultforFrag();

                        }catch(JSONException e){e.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );

        requestQueue.add(jor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            if (user_pref != "popular?") {
                if(HAS_VIEW)
                    Movie.position_fordetailfrag=0;
                user_pref = "popular?";
                makeRequest();
            }
            return true;
        }

        if (id == R.id.action_topRated)
        {
            if(user_pref!="top_rated?") {
                if(HAS_VIEW) Movie.position_fordetailfrag=0;
                user_pref = "top_rated?";
                makeRequest();
            }
            return true;
        }

        if(id==R.id.action_favorite)
        {
            if(user_pref!="favorite") {
                SQLiteOpenHelper dbHep = new MovieDbHelper(this);
                SQLiteDatabase db = dbHep.getReadableDatabase();
                Cursor cursor = db.query(Movie_Contract.MovieEntry.TABLE_NAME, new String[]{Movie_Contract.MovieEntry.ID, Movie_Contract.MovieEntry.IMAGE}, null, null, null, null, null);
                if (cursor.getCount() == 0)
                    Toast.makeText(this, "No favorite Movies Yet", Toast.LENGTH_SHORT).show();
                else {
                    user_pref = "favorite";
                    launchFavoritefragment();
                }
                db.close();
                dbHep.close();
                cursor.close();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    void launchFavoritefragment(){
        if(loader!=null) loader.dismiss();
        FavoriteFragment ff=new FavoriteFragment();
        try{
            getFragmentManager().beginTransaction().replace(R.id.list_container, ff).commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void itemClicked(int id) {

        Movie.position_fordetailfrag=id;
        if(Movie.movieArray==null)
        {
            makeRequest();
        }
        else if(HAS_VIEW)
        {
            MovieDetailFrag mdf=new MovieDetailFrag();
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            ft.replace(R.id.detail_container,mdf);
            ft.commit();

        }
        else
        {

            Intent i =new Intent(this,DetailActivity.class);
            i.putExtra("FRAGMENT","Movie");
            startActivity(i);
        }
    }

    @Override
    public void itemFavorite(int movieid) {
        Movie.posfdf=movieid;
        if(HAS_VIEW)
        {
            FavoriteDetailFragment fdf=new FavoriteDetailFragment();
            getFragmentManager().beginTransaction().replace(R.id.detail_container,fdf).commit();

        }else
        {
            Intent i=new Intent(this,DetailActivity.class);
            i.putExtra("FRAGMENT","favorite");
            startActivity(i);
        }
    }


    public void defaultforFrag()
    {
        MovieDetailFrag mdf=new MovieDetailFrag();
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.detail_container,mdf);
        try {
            ft.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}