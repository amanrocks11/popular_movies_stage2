package com.example.android.popularmovies2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class VideoAdapter extends ArrayAdapter<Video> {


    public VideoAdapter(Context context, List<Video> vilist) {
        super(context,0,vilist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.video_row_layout,parent,false);
        }
        ImageView play=(ImageView) convertView.findViewById(R.id.play_icon);
        Picasso.with(getContext()).load(R.drawable.ic_play_circle_filled_black_48dp).resize(50,50).into(play);
        TextView trailer=(TextView) convertView.findViewById(R.id.trailer);
        trailer.setText(Video.videarray[position].name);
        trailer.setTextSize(18);


        return convertView;
    }
}
