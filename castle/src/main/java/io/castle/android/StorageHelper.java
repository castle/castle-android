/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

class StorageHelper {
    private static final String STORAGE_PREFERENCE = "castle_storage";
    private static final String BUILD_KEY = "build_key";
    private static final String VERSION_KEY = "version_key";
    private static final String USER_ID_KEY = "user_id_key";
    private static final String DEVICE_ID_KEY = "device_id_key";
    private static final String USER_SIGNATURE_KEY = "user_signature_key";

    private SharedPreferences preferences;

    StorageHelper(Context context) {
        preferences = context.getSharedPreferences(STORAGE_PREFERENCE, Context.MODE_PRIVATE);
    }

    int getBuild() {
        return getPreferences().getInt(BUILD_KEY, -1);
    }

    void setBuild(int build) {
        getPreferencesEditor().putInt(BUILD_KEY, build).commit();
    }

    String getDeviceId() {
        String deviceId = getPreferences().getString(DEVICE_ID_KEY, null);
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString();
            setDeviceId(deviceId);
        }
        return deviceId;
    }

    private void setDeviceId(String deviceId) {
        getPreferencesEditor().putString(DEVICE_ID_KEY, deviceId).commit();
    }

    String getUserId() {
        return getPreferences().getString(USER_ID_KEY, null);
    }

    void setUserId(String userId) {
        getPreferencesEditor().putString(USER_ID_KEY, userId).commit();
    }

    private SharedPreferences getPreferences() {
        return preferences;
    }

    private SharedPreferences.Editor getPreferencesEditor() {
        return preferences.edit();
    }

    String getVersion() {
        return getPreferences().getString(VERSION_KEY, null);
    }

    void setVersion(String version) {
        getPreferencesEditor().putString(VERSION_KEY, version).commit();
    }

    String getUserSignature() {
        return getPreferences().getString(USER_SIGNATURE_KEY, null);
    }

    void setUserSignature(String signature) {
        getPreferencesEditor().putString(USER_SIGNATURE_KEY, signature).commit();
    }
}
