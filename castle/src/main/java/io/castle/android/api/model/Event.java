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

    String event;
    String type;
    @SerializedName("user_id")
    String userId;
    Map<String, String> properties = new HashMap<>();
    Context context;
    String timestamp;

    public Event(String event) {
        this.context = Context.create();
        this.timestamp = Utils.getTimestamp();
        this.userId = Castle.userId();
        this.event = event;
        this.type = EVENT_TYPE_EVENT;
    }

    public Event(String event, Map<String,String> traits) {
        this(event);
        this.properties = traits;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
