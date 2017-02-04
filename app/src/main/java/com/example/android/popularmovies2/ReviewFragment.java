package com.example.android.popularmovies2;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import com.example.android.popularmovies2.utility.ExpendableHeightListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {


    List<Review> relist;
    ReviewAdapter rAdap;
    ExpendableHeightListView majorList;
    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {
            relist= Arrays.asList((Review[]) savedInstanceState.getSerializable("REVIEW"));
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_review, container, false);
        majorList=(ExpendableHeightListView) view.findViewById(R.id.review_list);
        rAdap=new ReviewAdapter(getActivity(),relist);
        majorList.setAdapter(rAdap);
        majorList.setExpanded(true);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("REVIEW",Review.review);
        super.onSaveInstanceState(outState);
    }


}
