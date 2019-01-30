/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import android.os.Build;

/**
 * Model class for os version information, included in all events
 */
class OS extends Version {

    private OS(String name, String version) {
        super(name, version);
    }

    static OS create() {
        return new OS("Android", Build.VERSION.RELEASE);
    }
}
