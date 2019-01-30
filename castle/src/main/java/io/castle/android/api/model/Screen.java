/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for screen information, included in all events
 */
public class Screen {
    @SerializedName("width")
    int width;
    @SerializedName("height")
    int height;
    @SerializedName("density")
    float density;

    private Screen(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        density = displayMetrics.density;
    }

    public static Screen create(Context context) {
        return new Screen(context);
    }
}
