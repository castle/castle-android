package io.castle.android.api.model;

/**
 * Copyright (c) 2017 Castle
 */

public class Error {
    static final String ERROR_INVALID_PARAMETERS = "invalid_parameters";

    String type;
    String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return type + " " + message;
    }
}
