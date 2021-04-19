/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import io.castle.android.Castle;
import io.castle.android.Utils;

/**
 * Model class for events
 */
public class Event {
    public static final String EVENT_TYPE_EVENT = "track";
    public static final String EVENT_TYPE_SCREEN = "screen";
    public static final String EVENT_TYPE_IDENTIFY = "identify";

    @SerializedName("context")
    Context context;
    @SerializedName(value="event", alternate={"name"})
    String event;
    @SerializedName("timestamp")
    String timestamp;
    @SerializedName("type")
    String type;
    @SerializedName("user_id")
    String userId;
    @SerializedName("user_signature")
    String userSignature;

    /**
     * Create new event with specified name
     * @param event Event name
     */
    public Event(String event) {
        this.context = Castle.createContext();
        this.event = event;
        this.timestamp = Utils.getTimestamp();
        this.type = EVENT_TYPE_EVENT;
        this.userId = Castle.userId();
        this.userSignature = Castle.userSignature();
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
    public String getEvent() {
        return event;
    }
}
