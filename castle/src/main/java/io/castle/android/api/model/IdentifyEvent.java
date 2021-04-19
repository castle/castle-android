/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for identify events
 */
public class IdentifyEvent extends Event {

    @SerializedName("traits")
    Map<String, String> traits;

    /**
     * Create new identify event with provided user id
     * @param userId user id of the user
     */
    public IdentifyEvent(String userId) {
        super(null);
        this.type = EVENT_TYPE_IDENTIFY;
        this.userId = userId;
    }

    /**
     * Create new identify event with provided user id and traits
     * @param userId user id
     * @param traits traits
     */
    public IdentifyEvent(String userId, Map<String, String> traits) {
        super(null);
        this.type = EVENT_TYPE_IDENTIFY;
        this.userId = userId;
        this.traits = new HashMap<>(traits);
    }
}
