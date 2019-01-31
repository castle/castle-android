package io.castle.android.api.model;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright (c) 2017 Castle
 */
public abstract class Version {
    @SerializedName("version")
    protected String version;
    @SerializedName("name")
    protected String name;

    Version(String name, String version) {
        this.name = name;
        this.version = version;
    }
}
