package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import io.castle.android.Castle;
import io.castle.android.Utils;

/**
 * Copyright (c) 2017 Castle
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

    public Event(String event) {
        this.context = Castle.createContext();
        this.event = event;
        this.timestamp = Utils.getTimestamp();
        this.type = EVENT_TYPE_EVENT;
        this.userId = Castle.userId();
    }

    public Event(String event, Map<String,String> traits) {
        this(event);
        this.properties = new HashMap<>(traits);
    }

    public String getType() {
        return type;
    }

    public String getEvent() {
        return event;
    }
}
