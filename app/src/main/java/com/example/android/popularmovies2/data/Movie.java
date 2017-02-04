package com.example.android.popularmovies2.data;

/**
 * Created by aman on 04-02-2017.
 */

import java.io.Serializable;

public class Movie implements Serializable{
    String mTitle;
    String mRelDate;
    String vote_avg;
    String plotsynop;
    String imgPath;
    String videoPath;
    String reviewPath;
    String _id;
    int position_fordb;
    public static int posfdf;
    public static int position_fordetailfrag;
    public static final String api_key="api_key=";


    public static Movie[] movieArray;

    public void setposition_fordb(int pdb){
        position_fordb=pdb;
    }

    public int getposition_fordb()
    {
        return position_fordb;
    }


    public  void set_id(String id)
    {
        _id=id;
    }

    public String get_id()
    {
        return _id;
    }

    public String getReviewPath() {
        return reviewPath;
    }

    public void setReviewPath(String reviewPath) {
        this.reviewPath = reviewPath;
    }

    public void setmmTitle(String title) {
        mTitle = title;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getmmTitle() {
        return mTitle;
    }

    public String getmRelDate() {
        return mRelDate;
    }

    public void setmRelDate(String reldate) {
        mRelDate = reldate;
    }

    public String getVoteAvg() {
        return vote_avg;
    }

    public void setVoteAvg(String voteavg) {
        vote_avg = voteavg;
    }

    public String getPlotsynop() {
        return plotsynop;
    }

    public void setPlotsynop(String psn) {
        plotsynop = psn;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgpath) {
        imgPath = imgpath;
    }
}
