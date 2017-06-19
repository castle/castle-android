package io.castle.android.api.model;

import android.os.Build;

import io.castle.android.Castle;

/**
 * Copyright (c) 2017 Castle
 */

class Device {
    private String type;
    private String name;
    private String os;
    private String version;
    private String manufacturer;
    private String model;
    private String id;

    static Device create() {
        Device device = new Device();

        device.id = Build.MODEL;
        device.name = Build.MODEL;
        device.type = Build.DEVICE;
        device.os = "Android";
        device.version = Build.VERSION.RELEASE;
        device.manufacturer = Build.MANUFACTURER;
        device.model = Build.MODEL;
        device.id = Castle.deviceIdentifier();

        return device;
    }
}
