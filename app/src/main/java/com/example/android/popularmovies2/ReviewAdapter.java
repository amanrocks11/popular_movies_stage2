package com.example.android.popularmovies2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Activity activity, List<Review> relist) {
        super(activity,0,relist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.review_row_layout,parent,false);
        }
        TextView content=(TextView) convertView.findViewById(R.id.content);
        content.setText(Review.review[position].content);
        TextView author=(TextView) convertView.findViewById(R.id.author);
        author.setText(Review.review[position].author);
        return convertView;
    }
}
