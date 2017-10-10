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
    private String os;
    private String type;
    private String version;

    public static Device create() {
        Device device = new Device();

        device.id = Castle.deviceIdentifier();
        device.manufacturer = Build.MANUFACTURER;
        device.model = Build.MODEL;
        device.name = Build.MODEL;
        device.os = "Android";
        device.type = Build.DEVICE;
        device.version = Build.VERSION.RELEASE;

        return device;
    }
}
