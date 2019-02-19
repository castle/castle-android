/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import io.castle.android.BuildConfig;
import io.castle.android.Castle;

/**
 * Model class for library version information, included in all events
 */
class LibraryVersion extends Version {

    @SerializedName("user_agent")
    String userAgent;

    private LibraryVersion(String name, String version) {
        super(name, version);
        this.userAgent = Castle.userAgent();
    }

    static LibraryVersion create() {
        return new LibraryVersion("castle-android", BuildConfig.VERSION_NAME);
    }
}
