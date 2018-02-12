package io.castle.android.api.model;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * Copyright (c) 2017 Castle
 */
public class Screen {
    int width;
    int height;
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
