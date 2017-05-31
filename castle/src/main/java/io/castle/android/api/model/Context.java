package io.castle.android.api.model;

/**
 * Copyright (c) 2017 Castle
 */

public class Context {
    Device device;

    private Context() {
        this.device = Device.create();
    }

    static Context create() {
        return new Context();
    }
}
