package io.castle.android;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.List;

/**
 * Copyright (c) 2017 Castle
 */
public class Configuration {
    private String publishableKey;
    private boolean screenTrackingEnabled = true;
    private boolean debugLoggingEnabled = false;
    private int flushLimit = 20;
    private int maxQueueLimit = 1000;
    private List<String> whiteList;

    public Configuration(Application application) {
        try {
            ApplicationInfo applicationInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            this.publishableKey = bundle.getString("castle_publishable_key");
        } catch (PackageManager.NameNotFoundException e) {
            CastleLogger.e("Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            CastleLogger.e("Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }

    public void publishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String publishableKey() {
        return publishableKey;
    }

    public void screenTrackingEnabled(boolean enabled) {
        this.screenTrackingEnabled = enabled;
    }

    public void debugLoggingEnabled(boolean enabled) {
        this.debugLoggingEnabled = enabled;
    }

    public boolean screenTrackingEnabled() {
        return screenTrackingEnabled;
    }

    public boolean debugLoggingEnabled() {
        return debugLoggingEnabled;
    }

    public void baseURLWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    public List<String> baseURLWhiteList() {
        return whiteList;
    }

    public int flushLimit() {
        return flushLimit;
    }

    public void flushLimit(int flushLimit) {
        this.flushLimit = flushLimit;
    }

    public int maxQueueLimit() {
        return maxQueueLimit;
    }

    public void maxQueueLimit(int maxQueueLimit) {
        this.maxQueueLimit = maxQueueLimit;
    }
}
