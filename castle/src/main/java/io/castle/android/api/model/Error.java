/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for Castle API errors
 */
public class Error {
    @SerializedName("message")
    String message;
    @SerializedName("type")
    String type;

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " " + message;
    }
}
