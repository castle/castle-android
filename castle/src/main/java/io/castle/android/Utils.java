/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.castle.android.api.model.CustomEvent;
import io.castle.android.api.model.Event;
import io.castle.android.api.model.ScreenEvent;

public class Utils {
    private static SimpleDateFormat formatter = null;
    private static Gson gson;

    private static String formatDate(Date date) {
        if (formatter == null) {
            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            formatter = new SimpleDateFormat(pattern, new Locale("en", "US", "POSIX"));
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
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
            gsonBuilder.registerTypeAdapter(CustomEvent.class, eventAdapter);
            gsonBuilder.registerTypeAdapter(ScreenEvent.class, eventAdapter);

            gson = gsonBuilder.create();
        }
        return gson;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError("Package not found: " + context.getPackageName());
        }
    }

    private static ApplicationInfo getApplicationInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError("Package not found: " + context.getPackageName());
        }
    }

    static String getApplicationName(Context context) {
        return (String) context.getPackageManager().getApplicationLabel(getApplicationInfo(context));
    }

    static String getApplicationVersion(Context context) {
        return getPackageInfo(context).versionName;
    }

    static int getApplicationVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    static String sanitizeHeader(String string) {
        StringBuilder stringBuilder = new StringBuilder(string.length());
        for (int i = 0, length = string.length(); i < length; i++) {
            char c = string.charAt(i);
            if (c == '\t' || (c > '\u001f' && c < '\u007f')) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    static boolean isWellformedJwtToken(String jwt) {
        if (jwt != null && !jwt.isEmpty() && jwt.contains("")) {

            return true;
        }
        return false;
    }
}
