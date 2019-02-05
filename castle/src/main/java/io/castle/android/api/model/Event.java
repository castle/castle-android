/*
 * Copyright (c) 2017 Castle
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
    public static String EVENT_TYPE_EVENT = "track";
    public static String EVENT_TYPE_SCREEN = "screen";
    public static String EVENT_TYPE_IDENTIFY = "identify";

    @SerializedName("context")
    Context context;
    @SerializedName(value="event", alternate={"name"})
    String event;
    @SerializedName(value="properties", alternate={"traits"})
    Map<String, String> properties;
    @SerializedName("timestamp")
    String timestamp;
    @SerializedName("type")
    String type;
    @SerializedName("user_id")
    String userId;

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
    }

    /**
     * Create new event with specified name and traits
     * @param event Event name
     * @param traits Event traits
     */
    public Event(String event, Map<String,String> traits) {
        this(event);
        this.properties = new HashMap<>(traits);
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
