/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import android.app.Activity;

import java.util.Map;

public class ScreenEvent extends Event {

    public ScreenEvent(String name) {
        super(name);
        this.type = EVENT_TYPE_SCREEN;
    }

    public ScreenEvent(Activity activity) {
        this(activity.getTitle().toString());
    }

    public ScreenEvent(String name, Map<String, String> properties) {
        super(name, properties);
        this.type = EVENT_TYPE_SCREEN;
    }
}
