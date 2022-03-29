/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.castle.android.api.model.CustomEvent;
import io.castle.android.api.model.Event;
import io.castle.android.api.model.ScreenEvent;
import io.castle.android.api.model.UserJwt;
import io.castle.highwind.android.Highwind;
import io.castle.android.queue.EventQueue;

/**
 * This class is the main entry point for using the Castle SDK and provides methods for tracking events, screen views, manual flushing of the event queue, allowlisting behaviour and resetting.
 */
public class Castle {
    public static final String requestTokenHeaderName = "X-Castle-Request-Token";

    private static Castle instance;
    private CastleConfiguration configuration;
    private EventQueue eventQueue;
    private StorageHelper storageHelper;
    private CastleActivityLifecycleCallbacks activityLifecycleCallbacks;
    private CastleComponentCallback componentCallbacks;
    private Highwind highwind;

    private String appVersion;
    private int appBuild;
    private String appName;

    private Castle(Application application, CastleConfiguration castleConfiguration) {
        setup(application, castleConfiguration);
    }

    public static String encodeEvent(Event event) {
        if (event instanceof ScreenEvent) {
            return instance.highwind.encodeScreenEvent(event.getToken(), Utils.getGsonInstance().toJson(event));
        }
        return instance.highwind.encodeCustomEvent(event.getToken(), Utils.getGsonInstance().toJson(event));
    }

    public static String encodeUser(String userJwt) {
        return instance.highwind.encodeUserJwtPayloadSet(Utils.getGsonInstance().toJson(new UserJwt(userJwt)));
    }

    public static String encodePayload(String userPayload, List<String> eventPayloads) {
        return instance.highwind.encodePayload(publishableKey(), userPayload, eventPayloads);
    }

    private void setup(Application application, CastleConfiguration configuration) {
        Context context = application.getApplicationContext();

        // Get the current version.
        appVersion = Utils.getApplicationVersion(application);
        appBuild = Utils.getApplicationVersionCode(application);
        appName = Utils.getApplicationName(application);

        this.storageHelper = new StorageHelper(context);
        this.configuration = configuration;
        this.eventQueue = new EventQueue(context);
        this.highwind = new Highwind(context, BuildConfig.VERSION_NAME, storageHelper.getDeviceId(), buildUserAgent(), configuration.publishableKey(), storageHelper.getDeviceIdSource());
    }

    private void registerLifeCycleCallbacks(Application application) {
        activityLifecycleCallbacks = new CastleActivityLifecycleCallbacks();
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        componentCallbacks = new CastleComponentCallback();
        application.registerComponentCallbacks(componentCallbacks);

        // Get the previous recorded version.
        String previousVersion = storageHelper.getVersion();
        int previousBuild = storageHelper.getBuild();

        // Check and track Application Installed or Application Updated.
        if (previousBuild == -1) {
            custom("Application Installed");
        } else if (appBuild != previousBuild) {
            custom("Application Updated");
        }

        // Track Application Opened.
        custom("Application Opened");

        flush();

        // Update the recorded version.
        storageHelper.setVersion(appVersion);
        storageHelper.setBuild(appBuild);
    }

    private String id() {
        return highwind.token();
    }

    /**
     * Configure Castle using the provided configuration
     * @param application Application instance
     * @param configuration CastleConfiguration
     */
    public static void configure(Application application, CastleConfiguration configuration) {
        if (instance == null) {
            if (configuration.publishableKey() == null || !configuration.publishableKey().startsWith("pk_") || configuration.publishableKey().length() != 35) {
                throw new RuntimeException("You must provide a valid Castle publishable key when initializing the SDK.");
            }
            instance = new Castle(application, configuration);
            instance.registerLifeCycleCallbacks(application);
        }
    }

    /**
     * Configure Castle with default configuration using publishable key
     * @param application Application instance
     * @param publishableKey Castle publishable key
     */
    public static void configure(Application application, String publishableKey) {
        if (instance == null) {
            instance = new Castle(application, new CastleConfiguration.Builder().publishableKey(publishableKey).build());
            instance.registerLifeCycleCallbacks(application);
        }
    }

    /**
     * Configure Castle with provided configuration and publishable key
     * @param application Application instance
     * @param publishableKey Castle publishable key
     * @param configuration CastleConfiguration instance
     */
    public static void configure(Application application, String publishableKey, CastleConfiguration configuration) {
        if (instance == null) {
            instance = new Castle(application, new CastleConfiguration.Builder(configuration).publishableKey(publishableKey).build());
            instance.registerLifeCycleCallbacks(application);
        }
    }

    /**
     * Configure Castle with default configuration, will try to get publishable key from AndroidManifest meta tag castle_publishable_key
     * @param application Application instance
     */
    public static void configure(Application application) {
        try {
            ApplicationInfo applicationInfo =
                    application.getPackageManager()
                            .getApplicationInfo(application.getPackageName(),
                                    PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            String publishableKey = bundle.getString("castle_publishable_key");

            configure(application, new CastleConfiguration.Builder().publishableKey(publishableKey).build());
        } catch (PackageManager.NameNotFoundException e) {
            CastleLogger.e("Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            CastleLogger.e("Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }

    /**
     * Track custom event with a specified name
     * @param event Event name
     */
    public static void custom(String event) {
        if (event == null || event.isEmpty()) {
            return;
        }
        track(new CustomEvent(event));
    }

    /**
     * Track custom event with a specified name
     * @param event Event name
     */
    public static void custom(String event, Map<String, Object> properties) {
        if (event == null || event.isEmpty()) {
            return;
        }
        track(new CustomEvent(event, properties));
    }

    private static void track(Event event) {
        instance.eventQueue.add(event);
    }

    /**
     * Set user information with specified jwt encoded user. User jwt will be persisted.
     * @param userJwt encoded user jwt
     */
    public static void userJwt(String userJwt) {
        if (userJwt != null && !userJwt.isEmpty()) {
            instance.storageHelper.setUserJwt(userJwt);
        }
    }

    /**
     * Get userJwt from storage, returns null if not set
     * @return user id
     */
    public static String userJwt() {
        return instance.storageHelper.getUserJwt();
    }

    /**
     * Reset any stored user information and flush the event queue
     */
    public static void reset() {
        Castle.flush();
        Castle.userJwt(null);
    }

    /**
     * Track screen event with a specified name
     * @param name Event name
     */
    public static void screen(String name) {
        if (name == null || name.isEmpty()) {
            return;
        }
        track(new ScreenEvent(name));
    }

    /**
     * Track screen event using activity title
     * @param activity Activity
     */
    public static void screen(Activity activity) {
        track(new ScreenEvent(activity));
    }

    /**
     * Get configured publishable key
     * @return publishable key
     */
    public static String publishableKey() {
        return instance.configuration.publishableKey();
    }

    /**
     * Get debug logging enabled
     * @return true of debug logging is enabled
     */
    public static boolean debugLoggingEnabled() {
        return instance.configuration.debugLoggingEnabled();
    }

    /**
     * Get base url
     * @return Base url
     */
    public static String baseUrl() {
        return instance.configuration.baseUrl();
    }

    /**
     * Get request token
     * @return request token
     */
    public static String createRequestToken() {
        return instance.id();
    }

    /**
     * Get configuration
     * @return configuration
     */
    public static CastleConfiguration configuration() {
        return instance.configuration;
    }

    /**
     * Force a flush of the event queue, even if the flush limit hasn't been reached
     */
    public static void flush() {
        instance.eventQueue.flush();
    }

    /**
     * Force a flush if needed for a specific url, flushes if url is allowlisted
     */
    public static boolean flushIfNeeded(String url) {
        // Flush if request to allowlisted url
        if (isUrlAllowlisted(url)) {
            flush();
            return true;
        }
        return false;
    }

    /**
     * Get Castle headers for a specific url, returns non-empty when url is allowlisted
     */
    public static Map<String, String> headers(String url) {
        Map<String, String> headers = new HashMap<>();

        if (isUrlAllowlisted(url)) {
            headers.put(requestTokenHeaderName, Castle.createRequestToken());
        }

        return headers;
    }

    /**
     * Get Castle OkHttp interceptor
     */
    public static CastleInterceptor castleInterceptor() {
        return new CastleInterceptor();
    }

    /**
     * Determine if a given url is allowlisted
     * @return url allowlist status
     */
    static boolean isUrlAllowlisted(String urlString) {
        try {
            URL url = new URL(urlString);
            String baseUrl = url.getProtocol() + "://" + url.getHost() + "/";

            if (Castle.configuration().baseURLAllowList() != null && !Castle.configuration().baseURLAllowList().isEmpty()) {
                if (Castle.configuration().baseURLAllowList().contains(baseUrl)) {
                    return true;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get the current size of the event queue
     * @return The current size of the event queue
     */
    public static int queueSize() {
        return instance.eventQueue.size();
    }

    /**
     * Check if queue is being flushed
     * @return True if flushing is in progress
     */
    static boolean isFlushing() {
        return instance.eventQueue.isFlushing();
    }

    /**
     * Destroy instance of the Castle SDK
     */
    public static void destroy(Application application) {
        if (instance != null) {
            instance.eventQueue.destroy();
            instance.unregisterLifeCycleCallbacks(application);
            instance.unregisterComponentCallbacks(application);
            instance = null;
        }
    }

    /**
     * Get current app versionCode
     * @return current build
     */
    static int getCurrentBuild() {
        return instance.appBuild;
    }


    /**
     * Get custom user agent used for requests sent to Castle API
     * @return User agent string in format application name/version (versionCode) (Castle library version; Android version; Device name)
     */
    public static String userAgent() {
        return instance.buildUserAgent();
    }

    private String buildUserAgent() {
        return Utils.sanitizeHeader(String.format(Locale.US, "%s/%s (%d) (Castle %s; Android %s; %s %s)", appName, appVersion, appBuild, BuildConfig.VERSION_NAME, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.MODEL));
    }

    /**
     * Get current app versionName
     * @return current version
     */
    static String getCurrentVersion() {
        return instance.appVersion;
    }

    private void unregisterLifeCycleCallbacks(Application application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    private void unregisterComponentCallbacks(Application application) {
        application.unregisterComponentCallbacks(componentCallbacks);
    }
}
