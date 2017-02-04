package com.example.android.popularmovies2;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Arrays;
import java.util.List;

import com.example.android.popularmovies2.utility.ExpendableHeightListView;


public class VideoFragment extends Fragment {


    List<Video> vilist;
    VideoAdapter vAdap;
    ExpendableHeightListView majorList;
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {
            vilist= Arrays.asList((Video[]) savedInstanceState.getSerializable("VIDEO"));
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_video, container, false);
        majorList=(ExpendableHeightListView)view.findViewById(R.id.trailer_list);
        vAdap=new VideoAdapter(getActivity(),vilist);
        vAdap.notifyDataSetChanged();
        majorList.setAdapter(vAdap);
        majorList.setExpanded(true);


        majorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+Video.videarray[position].key)));

            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("VIDEO",Video.videarray);
        super.onSaveInstanceState(outState);
    }


}