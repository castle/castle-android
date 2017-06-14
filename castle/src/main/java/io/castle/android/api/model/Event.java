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

    public String getEvent() {
        return event;
    }
}
