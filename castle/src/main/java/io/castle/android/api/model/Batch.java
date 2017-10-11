package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.castle.android.Utils;

/**
 * Copyright (c) 2017 Castle
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
