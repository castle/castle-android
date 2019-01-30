/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;
import java.util.TimeZone;

import io.castle.android.Castle;

public class Context {
    @SerializedName("device")
    Device device;
    @SerializedName("os")
    OS os;
    @SerializedName("library")
    LibraryVersion library;
    @SerializedName("timezone")
    String timezone;
    @SerializedName("locale")
    String locale;
    @SerializedName("screen")
    Screen screen;
    @SerializedName("network")
    Network network;

    private Context(android.content.Context context) {
        this.device = Device.create();
        this.os = OS.create();
        this.timezone = TimeZone.getDefault().getID();
        this.locale = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
        this.screen = Screen.create(context);
        this.library = LibraryVersion.create();
        this.network = Network.create(context);
    }

    public static Context create(android.content.Context context) {
        return new Context(context);
    }
}
