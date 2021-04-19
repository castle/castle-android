/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import android.content.ComponentCallbacks2;
import android.content.pm.PackageInfo;
import android.content.res.*;
import android.content.res.Configuration;

import java.util.HashMap;
import java.util.Map;

class CastleComponentCallback implements ComponentCallbacks2 {
    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // We're in the Background
            Castle.track("Application Closed");
            Castle.flush();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {}

    @Override
    public void onLowMemory() {}
}
