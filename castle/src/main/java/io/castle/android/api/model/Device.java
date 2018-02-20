package io.castle.android.api.model;

import android.os.Build;

import io.castle.android.Castle;

/**
 * Copyright (c) 2017 Castle
 */

class Device {
    private String id;
    private String manufacturer;
    private String model;
    private String name;
    private String type;

    public static Device create() {
        Device device = new Device();

        device.id = Castle.clientId();
        device.manufacturer = Build.MANUFACTURER;
        device.model = Build.MODEL;
        device.name = Build.MODEL;
        device.type = Build.DEVICE;

        return device;
    }
}
