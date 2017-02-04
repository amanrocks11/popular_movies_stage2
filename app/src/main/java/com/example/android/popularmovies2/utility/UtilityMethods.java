package com.example.android.popularmovies2.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilityMethods {

    /*
    Method to checks whether Device is connected or trying to connect to Internet.
    params-> Context
    Returns-> a boolean variable determining  whether device is -connected or trying to connect- or can not connect to internet.
     */

    public static boolean isNetworkAvailable(Context c)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo active=connectivityManager.getActiveNetworkInfo();
        return active!=null && active.isConnectedOrConnecting();
    }
}
