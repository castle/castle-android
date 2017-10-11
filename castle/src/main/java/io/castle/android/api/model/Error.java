package io.castle.android.api.model;

/**
 * Copyright (c) 2017 Castle
 */

public class Error {
    String message;
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
