package com.loopedlabs.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtils {
    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Log.d("NetUtils", (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) ? "Device Online" : "Device Offline");
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
