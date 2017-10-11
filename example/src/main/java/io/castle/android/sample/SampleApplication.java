package io.castle.android.sample;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import io.castle.android.Castle;
import io.castle.android.Configuration;

/**
 * Copyright (c) 2017 Castle
 */

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Create configuration object
        Configuration configuration = new Configuration(this);

        // Enable the desired functionality
        configuration.publishableKey("pk_btApAXqt1jpJtEARf1stsnvyov6czPmn");
        configuration.screenTrackingEnabled(true); // Default: true
        configuration.debugLoggingEnabled(true); // Default: false

        List<String> whitelist = Arrays.asList(new String[] { "https://api.castle.io/" });
        configuration.baseURLWhiteList(whitelist);

        // Setup Castle SDK with provided configuration
        Castle.setupWithConfiguration(this, configuration);

        // Setup Castle SDK with default configuration
        Castle.setupWithDefaultConfiguration(this); // Reads appId from manifest meta tag
    }
}
