package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright (c) 2017 Castle
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
