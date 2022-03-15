/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import io.castle.android.Castle;
import io.castle.android.CastleLogger;

/**
 * Model class for events
 */
public class CustomEvent extends Event {
    @SerializedName("properties")
    Map<String, Object> properties;

    public CustomEvent(String name) {
        this(name, null);
    }

    /**
     * Create new event with specified name
     *
     * @param name Event name
     */
    public CustomEvent(String name, Map<String, Object> properties) {
        super(name);
        this.type = EVENT_TYPE_CUSTOM;

        boolean valid = propertiesContainValidData(properties);
        if(!valid) {
            CastleLogger.e("Properties dictionary contains invalid data. Supported types are: String, Integer, Float, Double, Map & Null");
        } else {
            this.properties = properties;
        }
    }

    @Override
    public String encode() {
        return Castle.encodeEvent(this);
    }

    public static boolean propertiesContainValidData(Map<String, Object> properties) {
        // Check if map is null
        if(properties == null) {
            return false;
        }

        // Iterate through the contents and make sure there's no unsupported data types
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            Object value = entry.getValue();

            // If the value of of any other type than NSNumber, NSString or NSNull: validation failed
            if (!(value instanceof String
                    || value instanceof Integer
                    || value instanceof Float
                    || value instanceof Double
                    || value == null)
            ) {
                CastleLogger.e("Properties map contains invalid data.");
                return false;
            }
        }

        // No data in the traits map was caught by the validation i.e. it's valid
        return true;
    }
}
