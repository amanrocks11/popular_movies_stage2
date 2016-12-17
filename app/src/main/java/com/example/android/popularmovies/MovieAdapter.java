package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aman on 16-12-2016.
 */

class MovieAdapter extends ArrayAdapter<MovieData> {
    private Context mContext;

    MovieAdapter(Activity context, List<MovieData> movieDataList) {
        super(context,0,movieDataList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ImageView mImageView;
        MovieData movieData = getItem(position);
        assert movieData != null;
        String url = "http://image.tmdb.org/t/p/w185"+movieData.getPosterPath();
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            mImageView = new ImageView(mContext);
            mImageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setAdjustViewBounds(true);

        } else {
            mImageView = (ImageView) convertView;
        }
        Picasso.with(getContext()).load(url.trim()).into(mImageView);
        return mImageView;
    }
}
