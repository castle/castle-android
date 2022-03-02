/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import android.app.Activity;

import java.util.Map;

/**
 * Model class for screen events
 */
public class ScreenEvent extends Model {

    /**
     * Create new screen event with provided name
     * @param name screen name
     */
    public ScreenEvent(String name) {
        super(name);
        this.type = EVENT_TYPE_SCREEN;
    }

    /**
     * Create new screen using Activity title
     * @param activity Activity
     */
    public ScreenEvent(Activity activity) {
        this(activity.getTitle() != null ? activity.getTitle().toString() : activity.getClass().getSimpleName());
    }
}
