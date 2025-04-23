/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

class CastleActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(@NotNull Activity activity, Bundle bundle) {}

    @Override
    public void onActivityStarted(@NotNull Activity activity) {
        if (Castle.configuration().screenTrackingEnabled()) {
            Castle.screen(activity);
        }
    }

    @Override
    public void onActivityResumed(@NotNull Activity activity) {}

    @Override
    public void onActivityPaused(@NotNull Activity activity) {}

    @Override
    public void onActivityStopped(@NotNull Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle bundle) {}

    @Override
    public void onActivityDestroyed(@NotNull Activity activity) {}
}
