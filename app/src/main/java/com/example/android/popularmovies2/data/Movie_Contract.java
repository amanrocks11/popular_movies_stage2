package com.example.android.popularmovies2.data;

import android.provider.BaseColumns;

public class Movie_Contract {


    public static  final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME="favorite";
        public static String URI="content://com.example.android.popularmovies";


        public static String ID="_id";
        public static String TITLE="title";
        public static String RELEASE="relaeasedate";
        public static String VOTE="vote";
        public static String PLOT="plot_synopsis";
        public static String IMAGE="imagepath";
        public static String VIDEO="videopath";
        public static String REVIEW="reviewpath";
        public static String MOVIE_ID="mid";
        public static String Position="position";



    }
}
