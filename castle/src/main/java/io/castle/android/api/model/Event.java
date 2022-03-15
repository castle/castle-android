/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import io.castle.android.Castle;
import io.castle.android.CastleLogger;
import io.castle.android.Utils;

/**
 * Model class for events
 */
public abstract class Event {
    public static final String EVENT_TYPE_CUSTOM = "custom";
    public static final String EVENT_TYPE_SCREEN = "screen";

    @SerializedName("name")
    String name;
    @SerializedName("timestamp")
    String timestamp;
    @SerializedName("type")
    String type;
    @SerializedName("token")
    String token;

    /**
     * Create new event with specified name
     * @param name Event name
     */
    public Event(String name) {
        this.name = name;
        this.timestamp = Utils.getTimestamp();
        this.token = Castle.createRequestToken();
    }

    /**
     * @return Event type
     */
    public String getType() {
        return type;
    }

    /**
     * @return Event name
     */
    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getToken() {
        return token;
    }

    public String encode() {
        return Castle.encodeEvent(this);
    }
}
