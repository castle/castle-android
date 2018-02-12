package io.castle.android.api.model;

import android.os.Build;

/**
 * Copyright (c) 2017 Castle
 */
class OS extends Version {

    private OS(String name, String version) {
        super(name, version);
    }

    static OS create() {
        return new OS("Android", Build.VERSION.RELEASE);
    }
}
