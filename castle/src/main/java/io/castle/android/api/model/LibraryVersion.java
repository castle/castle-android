/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api.model;

import io.castle.android.BuildConfig;

class LibraryVersion extends Version {

    private LibraryVersion(String name, String version) {
        super(name, version);
    }

    static LibraryVersion create() {
        return new LibraryVersion("castle-android", BuildConfig.VERSION_NAME);
    }
}
