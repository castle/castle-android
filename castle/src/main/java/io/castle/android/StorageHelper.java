package io.castle.android;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Copyright (c) 2017 Castle
 */

class StorageHelper {
    private static final String STORAGE_PREFERENCE = "castle_storage";
    private static final String BUILD_KEY = "build_key";
    private static final String VERSION_KEY = "version_key";
    private static final String USER_ID_KEY = "user_id_key";
    private static final String DEVICE_ID_KEY = "device_id_key";

    private SharedPreferences preferences;

    public StorageHelper(Context context) {
        preferences = context.getSharedPreferences(STORAGE_PREFERENCE, Context.MODE_PRIVATE);
    }

    String getIdentity() {
        return getPreferences().getString(USER_ID_KEY, null);
    }

    void setIdentity(String userId) {
        getPreferencesEditor().putString(USER_ID_KEY, userId).commit();

    }

    String getVersion() {
        return getPreferences().getString(VERSION_KEY, null);
    }

    void setVersion(String version) {
        getPreferencesEditor().putString(VERSION_KEY, version).commit();
    }

    int getBuild() {
        return getPreferences().getInt(BUILD_KEY, -1);
    }

    void setBuild(int build) {
        getPreferencesEditor().putInt(BUILD_KEY, build).commit();
    }

    String getDeviceId() {
        return getPreferences().getString(DEVICE_ID_KEY, UUID.randomUUID().toString());
    }

    private SharedPreferences getPreferences() {
        return preferences;
    }

    private SharedPreferences.Editor getPreferencesEditor() {
        return preferences.edit();
    }
}
