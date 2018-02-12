package io.castle.android.api.model;

import android.os.Build;

/**
 * Copyright (c) 2017 Castle
 */
public abstract class Version {
    protected String version;
    protected String name;

    protected Version(String name, String version) {
        this.name = name;
        this.version = version;
    }
}
