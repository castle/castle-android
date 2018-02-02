package io.castle.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.castle.android.api.EventAdapter;
import io.castle.android.api.model.Event;
import io.castle.android.api.model.IdentifyEvent;
import io.castle.android.api.model.ScreenEvent;

/**
 * Copyright (c) 2017 Castle
 */

public class Utils {
    private static String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
    private static Gson gson;

    public static String formatDate(Date date) {
        return formatter.format(date);
    }

    public static String getTimestamp() {
        return formatDate(new Date());
    }

    public static Gson getGsonInstance() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            EventAdapter eventAdapter = new EventAdapter();
            gsonBuilder.registerTypeAdapter(Event.class, eventAdapter);
            gsonBuilder.registerTypeAdapter(IdentifyEvent.class, eventAdapter);
            gsonBuilder.registerTypeAdapter(ScreenEvent.class, eventAdapter);

            gson = gsonBuilder.create();
        }
        return gson;
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError("Package not found: " + context.getPackageName());
        }
    }
}
