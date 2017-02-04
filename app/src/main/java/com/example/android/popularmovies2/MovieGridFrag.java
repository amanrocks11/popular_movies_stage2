package com.example.android.popularmovies2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.Arrays;
import java.util.List;

import com.example.android.popularmovies2.data.Movie;
/**
 * A simple {@link Fragment} subclass.
 */
public class MovieGridFrag extends Fragment {

    StaggeredGridView recipeListView;
    private GridView moviegrid;



    private ClickCallback movielistener;
    public List<Movie> movieList;

    public MovieGridFrag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity context) {
        this.movielistener=(ClickCallback) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState!=null)
            movieList= Arrays.asList((Movie[]) savedInstanceState.getSerializable("OLD"));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_movie_grid, container, false);
        recipeListView=(StaggeredGridView) view.findViewById(R.id.movie_display);
        MovieGridAdapter movieGridAdapter=new MovieGridAdapter(getActivity(),movieList);
        movieGridAdapter.notifyDataSetChanged();
        recipeListView.setAdapter(movieGridAdapter);
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(movielistener!=null)
                {
                    int postions=position;
                    movielistener.itemClicked(position);
                }
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("OLD",Movie.movieArray);
        super.onSaveInstanceState(outState);
    }


}
