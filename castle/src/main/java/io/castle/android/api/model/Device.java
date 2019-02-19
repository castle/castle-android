/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

import io.castle.android.Castle;

/**
 * Model class containing device information for a user. Included in all events
 */
class Device {
    @SerializedName("id")
    private String id;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("type")
    private String type;

    public static Device create() {
        Device device = new Device();

        device.id = Castle.clientId();
        device.manufacturer = Build.MANUFACTURER;
        device.model = Build.MODEL;
        device.type = Build.DEVICE;

        return device;
    }
}
