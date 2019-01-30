/*
 * Copyright (c) 2019 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.castle.android.Utils;

/**
 * Model class used as payload when sending a batch of events to the Castle API
 */
public class Batch {
    @SerializedName("batch")
    private ArrayList<Event> events;
    @SerializedName("sent_at")
    private String sentAt;

    public Batch() {
        sentAt = Utils.getTimestamp();
    }

    public void addEvents(List<Event> events) {
        if (this.events == null) {
            this.events = new ArrayList<>(events.size());
        }
        this.events.addAll(events);
    }
}
