/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.sample;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import io.castle.android.Castle;
import io.castle.android.CastleConfiguration;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        List<String> baseURLAllowlist = Arrays.asList(new String[] { "https://api.castle.io/" });

        // Create configuration object
        CastleConfiguration configuration = new CastleConfiguration.Builder()
                .publishableKey("pk_btApAXqt1jpJtEARf1stsnvyov6czPmn")
                .screenTrackingEnabled(true)
                .debugLoggingEnabled(true)
                .baseURLAllowlist(baseURLAllowlist)
                .build();

        // Setup Castle SDK with provided configuration
        Castle.configure(this, configuration);
    }
}
