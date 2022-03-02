/*
 * Copyright (c) 2019 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.castle.android.Castle;
import io.castle.android.CastleLogger;
import io.castle.android.Utils;
import io.castle.highwind.android.Highwind;

/**
 * Model class used as payload when sending a batch of events to the Castle API
 */
public class Monitor {
    @SerializedName("data")
    private String data;

    public static Monitor monitorWithEvents(List<Model> events) {
        if(events == null) {
            CastleLogger.e("Nil event array parameter provided. Won't flush events.");
            return null;
        }

        if(events.size() == 0) {
            CastleLogger.e("Empty event array parameter provided.");
            return null;
        }

        User user = Castle.user();
        if(user == null) {
            CastleLogger.e("No user id set, won't flush events.");
            return null;
        }

        Monitor monitor = new Monitor();

        List<String> encodedEvents = new ArrayList<>();
        for (Model event : events) {
            encodedEvents.add(event.encode());
        }

        monitor.data = Castle.encodePayload(user.encode(), encodedEvents);

        return monitor;
    }
}