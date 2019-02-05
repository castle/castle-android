/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for version information
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
