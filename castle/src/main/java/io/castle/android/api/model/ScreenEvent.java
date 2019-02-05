/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import android.app.Activity;

import java.util.Map;

/**
 * Model class for screen events
 */
public class ScreenEvent extends Event {

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
        this(activity.getTitle().toString());
    }

    /**
     * Create new screen event with provided name and properties
     * @param name user id
     * @param properties traits
     */
    public ScreenEvent(String name, Map<String, String> properties) {
        super(name, properties);
        this.type = EVENT_TYPE_SCREEN;
    }
}
