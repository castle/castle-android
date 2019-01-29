package io.castle.android;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.List;

/**
 * Copyright (c) 2017 Castle
 */
public class CastleConfiguration {
    private boolean debugLoggingEnabled = false;

    private int flushLimit = 20;
    private int maxQueueLimit = 1000;

    private String publishableKey;
    private boolean screenTrackingEnabled = true;

    private List<String> baseURLWhiteList;

    public CastleConfiguration() {
        this(new Builder());
    }

    private CastleConfiguration(Builder builder) {
        this.debugLoggingEnabled = builder.debugLoggingEnabled;
        this.flushLimit = builder.flushLimit();
        this.maxQueueLimit = builder.maxQueueLimit();
        this.publishableKey = builder.publishableKey();
        this.screenTrackingEnabled = builder.screenTrackingEnabled();
        this.baseURLWhiteList = builder.baseURLWhiteList();
    }

    public List<String> baseURLWhiteList() {
        return baseURLWhiteList;
    }

    public boolean debugLoggingEnabled() {
        return debugLoggingEnabled;
    }

    public int flushLimit() {
        return flushLimit;
    }

    public int maxQueueLimit() {
        return maxQueueLimit;
    }

    public String publishableKey() {
        return publishableKey;
    }

    public boolean screenTrackingEnabled() {
        return screenTrackingEnabled;
    }

    public static final class Builder {
        private boolean debugLoggingEnabled;
        private int flushLimit;
        private int maxQueueLimit;
        private String publishableKey;
        private boolean screenTrackingEnabled;
        private List<String> baseURLWhiteList;

        public Builder() {
            debugLoggingEnabled = false;
            flushLimit = 20;
            maxQueueLimit = 1000;
            screenTrackingEnabled = true;
        }

        public Builder(CastleConfiguration configuration) {
            debugLoggingEnabled = configuration.debugLoggingEnabled();
            flushLimit = configuration.flushLimit();
            maxQueueLimit = configuration.maxQueueLimit();
            publishableKey = configuration.publishableKey();
            screenTrackingEnabled = configuration.screenTrackingEnabled();
            baseURLWhiteList = configuration.baseURLWhiteList();
        }

        public Builder baseURLWhiteList(List<String> baseURLWhiteList) {
            this.baseURLWhiteList = baseURLWhiteList;

            return this;
        }

        public List<String> baseURLWhiteList() {
            return baseURLWhiteList;
        }

        public Builder debugLoggingEnabled(boolean enabled) {
            this.debugLoggingEnabled = enabled;

            return this;
        }

        public boolean debugLoggingEnabled() {
            return debugLoggingEnabled;
        }

        public int flushLimit() {
            return flushLimit;
        }

        public Builder flushLimit(int flushLimit) {
            this.flushLimit = flushLimit;

            return this;
        }

        public int maxQueueLimit() {
            return maxQueueLimit;
        }

        public Builder maxQueueLimit(int maxQueueLimit) {
            this.maxQueueLimit = maxQueueLimit;

            return this;
        }

        public Builder publishableKey(String publishableKey) {
            this.publishableKey = publishableKey;

            return this;
        }

        public String publishableKey() {
            return publishableKey;
        }

        public Builder screenTrackingEnabled(boolean enabled) {
            this.screenTrackingEnabled = enabled;

            return this;
        }

        public boolean screenTrackingEnabled() {
            return screenTrackingEnabled;
        }

        public CastleConfiguration build() {
            return new CastleConfiguration(this);
        }
    }
}
