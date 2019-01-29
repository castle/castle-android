package io.castle.android;

import android.util.Log;

/**
 * Copyright (c) 2017 Castle
 */

public class CastleLogger {
    private static final String TAG = "Castle";

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }

    public static void d(String message) {
        if (Castle.configuration().debugLoggingEnabled()) {
            Log.d(TAG, message);
        }
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }
}
