package io.castle.android.api.model;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

import io.castle.android.Castle;

/**
 * Copyright (c) 2017 Castle
 */

class Device {
    @SerializedName("id")
    private String id;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
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
