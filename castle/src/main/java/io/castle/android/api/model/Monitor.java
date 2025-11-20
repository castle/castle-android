/*
 * Copyright (c) 2019 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.castle.android.Castle;
import io.castle.android.CastleLogger;

/**
 * Model class used as payload when sending a batch of events to the Castle API
 */
public class Monitor {
    @SerializedName("data")
    private String data;

    public static Monitor monitorWithEvents(List<Event> events) {
        if(events == null) {
            CastleLogger.e("Nil event array parameter provided. Won't flush events.");
            return null;
        }

        if(events.isEmpty()) {
            CastleLogger.e("Empty event array parameter provided.");
            return null;
        }

        String userJwt = Castle.userJwt();
        if(userJwt == null) {
            CastleLogger.e("No user jwt set, won't flush events.");
            return null;
        }

        Monitor monitor = new Monitor();

        List<String> encodedEvents = new ArrayList<>();
        for (Event event : events) {
            encodedEvents.add(event.encode());
        }

        monitor.data = Castle.encodePayload(Castle.encodeUser(userJwt), encodedEvents);

        return monitor;
    }
}