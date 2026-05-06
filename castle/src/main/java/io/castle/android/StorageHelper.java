/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.google.gson.JsonSyntaxException;

import java.util.UUID;

import io.castle.highwind.android.Highwind;

class StorageHelper {
    private static final String STORAGE_PREFERENCE = "castle_storage";
    private static final String BUILD_KEY = "build_key";
    private static final String VERSION_KEY = "version_key";
    private static final String USER_JWT_KEY = "user_jwt_key";
    private static final String DEVICE_ID_KEY = "device_id_key";
    private static final String DEVICE_ID_SOURCE_KEY = "device_id_source_key";

    private final SharedPreferences preferences;

    StorageHelper(Context context) {
        preferences = context.getSharedPreferences(STORAGE_PREFERENCE, Context.MODE_PRIVATE);
    }

    int getBuild() {
        return preferences.getInt(BUILD_KEY, -1);
    }

    void setBuild(int build) {
        preferences.edit().putInt(BUILD_KEY, build).apply();
    }

    String getDeviceId() {
        String deviceId = preferences.getString(DEVICE_ID_KEY, null);
        if (deviceId == null) {
            Pair<String, Integer> id = DeviceIdUtils.deviceId();
            deviceId = id.first;
            setDeviceId(id.first);
            setDeviceIdSource(id.second);
        }
        return deviceId;
    }

    private void setDeviceId(String deviceId) {
        preferences.edit().putString(DEVICE_ID_KEY, deviceId).apply();
    }

    Integer getDeviceIdSource() {
        return preferences.getInt(DEVICE_ID_SOURCE_KEY, Highwind.ID_SOURCE_GENERATED);
    }

    private void setDeviceIdSource(int source) {
        preferences.edit().putInt(DEVICE_ID_SOURCE_KEY, source).apply();
    }

    String getUserJwt() {
        return preferences.getString(USER_JWT_KEY, null);
    }

    void setUserJwt(String userJwt) {
        preferences.edit().putString(USER_JWT_KEY, userJwt).apply();
    }

    String getVersion() {
        return preferences.getString(VERSION_KEY, null);
    }

    void setVersion(String version) {
        preferences.edit().putString(VERSION_KEY, version).apply();
    }
}
