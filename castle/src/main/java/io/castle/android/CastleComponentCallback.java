/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import android.content.ComponentCallbacks2;
import android.content.pm.PackageInfo;
import android.content.res.*;
import android.content.res.Configuration;

import java.util.HashMap;
import java.util.Map;

public class CastleComponentCallback implements ComponentCallbacks2 {
    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // We're in the Background
            Map<String, String> properties = new HashMap<>();
            properties.put("version", Castle.getCurrentVersion());
            properties.put("build", "" + Castle.getCurrentBuild());
            Castle.track("Application Closed", properties);
            Castle.flush();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {}

    @Override
    public void onLowMemory() {}
}
