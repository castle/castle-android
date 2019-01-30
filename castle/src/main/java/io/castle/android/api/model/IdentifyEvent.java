/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import java.util.Map;

/**
 * Model class for identify events
 */
public class IdentifyEvent extends Event {

    /**
     * Create new identify event with provided user id
     * @param userId user id of the user
     */
    public IdentifyEvent(String userId) {
        super(null);
        this.type = EVENT_TYPE_IDENTIFY;
        this.userId = userId;
    }

    /**
     * Create new identify event with provided user id and traits
     * @param userId user id
     * @param traits traits
     */
    public IdentifyEvent(String userId, Map<String, String> traits) {
        super(null, traits);
        this.type = EVENT_TYPE_IDENTIFY;
        this.userId = userId;
    }
}
