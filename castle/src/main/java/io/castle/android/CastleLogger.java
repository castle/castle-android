/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import android.util.Log;

/**
 * Internal logger
 */
public class CastleLogger {
    private static final String TAG = "Castle";

    /**
     * Send an error log message
     * @param message Message to be logged
     */
    public static void e(String message) {
        Log.e(TAG, message);
    }

    /**
     * Send an error log message and throwable
     * @param message Message to be logged
     * @param throwable Throwable to be logged
     */
    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }

    /**
     * Send an debug log message
     * @param message Message to be logged
     */
    public static void d(String message) {
        if (Castle.configuration().debugLoggingEnabled()) {
            Log.d(TAG, message);
        }
    }

    /**
     * Send an info log message
     * @param message Message to be logged
     */
    public static void i(String message) {
        Log.i(TAG, message);
    }
}
