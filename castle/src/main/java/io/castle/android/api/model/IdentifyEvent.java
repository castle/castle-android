package io.castle.android.api.model;

import java.util.Map;

/**
 * Copyright (c) 2017 Castle
 */

public class IdentifyEvent extends Event {
    public IdentifyEvent(String userId) {
        super(null);
        this.type = EVENT_TYPE_IDENTIFY;
        this.userId = userId;
    }

    public IdentifyEvent(String userId, Map<String, String> traits) {
        super(userId, traits);
    }
}
