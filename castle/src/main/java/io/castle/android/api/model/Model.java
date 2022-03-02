/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import io.castle.android.Castle;
import io.castle.android.CastleLogger;
import io.castle.android.Utils;

/**
 * Model class for events
 */
public abstract class Model {
    public static final String EVENT_TYPE_CUSTOM = "custom";
    public static final String EVENT_TYPE_SCREEN = "screen";

    @SerializedName("name")
    String name;
    @SerializedName("timestamp")
    String timestamp;
    @SerializedName("type")
    String type;
    @SerializedName("token")
    String token;

    /**
     * Create new event with specified name
     * @param name Event name
     */
    public Model(String name) {
        this.name = name;
        this.timestamp = Utils.getTimestamp();
        this.token = Castle.createRequestToken();
    }

    /**
     * @return Event type
     */
    public String getType() {
        return type;
    }

    /**
     * @return Event name
     */
    public String getName() {
        return name;
    }

    public static boolean propertiesContainValidData(Map<String, Object> traits) {
        // Check if map is null
        if(traits == null) {
            return false;
        }

        // Iterate through the contents and make sure there's no unsupported data types
        for (Map.Entry<String, Object> entry : traits.entrySet()) {
            Object value = entry.getValue();

            // If the value is a Map call the method recursively
            if (value instanceof Map) {
                try {
                    Map<String, Object> map = (Map<String, Object>) value;

                    // If the contents aren't valid we can return without continuing any futher
                    boolean valid = Model.propertiesContainValidData(map);
                    if (!valid) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }

            // If the value of of any other type than NSNumber, NSString or NSNull: validation failed
            if (!(value instanceof String
                    || value instanceof Integer
                    || value instanceof Float
                    || value instanceof Double
                    || value == null)
            ) {
                CastleLogger.e("Properties dictionary contains invalid data.");
                return false;
            }
        }

        // No data in the traits map was caught by the validation i.e. it's valid
        return true;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getToken() {
        return token;
    }

    public String encode() {
        return Castle.encodeEvent(this);
    }
}
