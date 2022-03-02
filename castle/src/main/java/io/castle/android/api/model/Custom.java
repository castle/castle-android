/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import io.castle.android.Castle;
import io.castle.android.Utils;
import io.castle.highwind.android.Highwind;

/**
 * Model class for events
 */
public class Custom extends Model {
    /**
     * Create new event with specified name
     *
     * @param name Event name
     */
    public Custom(String name) {
        super(name);
        this.type = EVENT_TYPE_CUSTOM;
    }

    @Override
    public String encode() {
        return Castle.encodeEvent(this);
    }
}
