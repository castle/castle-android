/*
 * Copyright (c) 2022 Castle
 */

package io.castle.android;

import android.media.MediaDrm;
import android.os.Build;
import android.util.Pair;

import java.security.MessageDigest;
import java.util.UUID;
import java.util.regex.Pattern;

import io.castle.highwind.android.Highwind;

public class DeviceIdUtils {
    private final static Pattern HEX_REGEX_PATTERN = Pattern.compile("^[0-9a-f]+$");
    private static final UUID WIDEVINE_UUID = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);

    public static Pair<String, Integer> deviceId() {
        try {
            String wideVineId = wideVineId();
            if (isValid(wideVineId)) {
                return new Pair<>(wideVineId, Highwind.ID_SOURCE_WIDEVINE);
            }
        } catch (Exception ignored) {}

        return new Pair<>(UUID.randomUUID().toString(), Highwind.ID_SOURCE_GENERATED);
    }

    static String wideVineId() {
        MediaDrm wvDrm = null;
        try {
            wvDrm = new MediaDrm(WIDEVINE_UUID);
            byte[] wideVineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(wideVineId);
            return byteArrayToHexString(md.digest());
        } catch (Exception e) {
            // Inspect exception
            return null;
        } finally {
            if (wvDrm != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    wvDrm.close();
                } else {
                    wvDrm.release();
                }
            }
        }
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (byte element : bytes) {
            buffer.append(String.format("%02x", element));
        }

        return buffer.toString();
    }

    private static boolean isValid(String hash) {
        if (hash == null) {
            return false;
        }
        if (hash.length() != 32) {
            return false;
        }
        boolean matches = HEX_REGEX_PATTERN.matcher(hash).matches();
        return matches;
    }
}
