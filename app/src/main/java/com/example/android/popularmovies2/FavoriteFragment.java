package com.example.android.popularmovies2;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.example.android.popularmovies2.data.Movie_Contract;
import com.example.android.popularmovies2.data.MovieDbHelper;
import com.example.android.popularmovies2.utility.FavoriteAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {


    StaggeredGridView grid;
    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity context) {
        this.clickListener=(ClickCallback) context;
        super.onAttach(context);
    }

    ClickCallback clickListener;

    Cursor cursor;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_movie_grid,container,false);
        grid=(com.etsy.android.grid.StaggeredGridView) view.findViewById(R.id.movie_display);
        SQLiteOpenHelper mdbh=new MovieDbHelper(getActivity());
        final SQLiteDatabase db=mdbh.getReadableDatabase();
        try {
            cursor = db.query(Movie_Contract.MovieEntry.TABLE_NAME, new String[]{Movie_Contract.MovieEntry.ID, Movie_Contract.MovieEntry.IMAGE, Movie_Contract.MovieEntry.Position, Movie_Contract.MovieEntry.MOVIE_ID}, null, null, null, null,null);
            final FavoriteAdapter adapter=new FavoriteAdapter(getActivity(),cursor);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if(clickListener!=null) {
                        cursor.moveToPosition(position);
                        clickListener.itemFavorite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Movie_Contract.MovieEntry.MOVIE_ID))));

                    }
                }
            });

        }catch (Exception e)
        {
            Log.e("MOVIE_DATABASE",String.valueOf(e));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        if(cursor!=null)
            cursor.close();
        super.onDestroy();
    }
}