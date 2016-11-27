/* $Id$ */
package com.muthuraj.chat.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.android.volley.VolleyError;

import java.nio.charset.Charset;

/**
 * Created by muthu-3955 on 27/11/16.
 */
public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String getErrorMessage(VolleyError error) {
        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
            return new String(error.networkResponse.data, Charset.defaultCharset());
        }
        return null;
    }
}
