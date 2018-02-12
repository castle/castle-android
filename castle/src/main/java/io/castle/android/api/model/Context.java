package io.castle.android.api.model;

import java.util.Locale;
import java.util.TimeZone;

import io.castle.android.Castle;

/**
 * Copyright (c) 2017 Castle
 */

public class Context {
    Device device;
    OS os;
    LibraryVersion library;
    String timezone;
    String locale;
    Screen screen;
    Network network;

    private Context(android.content.Context context) {
        this.device = Device.create();
        this.os = OS.create();
        this.timezone = TimeZone.getDefault().getID();
        this.locale = Locale.getDefault().toString();
        this.screen = Screen.create(context);
        this.library = LibraryVersion.create();
        this.network = Network.create(context);
    }

    public static Context create(android.content.Context context) {
        return new Context(context);
    }
}
