package com.byl.mvp.utils;

import android.util.Log;

import com.byl.mvp.BuildConfig;

public class LogUtil {
    private static String TAG = "mvp";

    public static void i(String message) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, message);
    }

    public static void e(String message) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, message);
    }

}
