/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonSyntaxException;

import java.util.UUID;

class StorageHelper {
    private static final String STORAGE_PREFERENCE = "castle_storage";
    private static final String BUILD_KEY = "build_key";
    private static final String VERSION_KEY = "version_key";
    private static final String USER_JWT_KEY = "user_jwt_key";
    private static final String DEVICE_ID_KEY = "device_id_key";

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
            deviceId = DeviceIdUtils.deviceId();
            setDeviceId(deviceId);
        }
        return deviceId;
    }

    private void setDeviceId(String deviceId) {
        getPreferencesEditor().putString(DEVICE_ID_KEY, deviceId).commit();
    }

    String getUserJwt() {
        return getPreferences().getString(USER_JWT_KEY, null);
    }

    void setUserJwt(String userJwt) {
        getPreferencesEditor().putString(USER_JWT_KEY, userJwt).commit();
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
}
