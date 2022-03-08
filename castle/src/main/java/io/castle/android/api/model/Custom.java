/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import io.castle.android.Castle;

/**
 * Model class for events
 */
public class Custom extends Event {
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
