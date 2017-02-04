package com.example.android.popularmovies2.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilityMethods {


    public static boolean isNetworkAvailable(Context c)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo active=connectivityManager.getActiveNetworkInfo();
        return active!=null && active.isConnectedOrConnecting();
    }
}
