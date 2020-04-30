/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.castle.android.api.model.Event;
import io.castle.android.api.model.IdentifyEvent;
import io.castle.android.api.model.ScreenEvent;
import io.castle.android.queue.EventQueue;

/**
 * This class is the main entry point for using the Castle SDK and provides methods for tracking events, screen views, manual flushing of the event queue, whitelisting behaviour and resetting.
 */
public class Castle {
    public static final String clientIdHeaderName = "X-Castle-Client-Id";

    private static Castle instance;
    private Application application;
    private String identifier;
    private CastleConfiguration configuration;
    private EventQueue eventQueue;
    private StorageHelper storageHelper;
    private CastleActivityLifecycleCallbacks activityLifecycleCallbacks;
    private CastleComponentCallback componentCallbacks;

    private String appVersion;
    private int appBuild;
    private String appName;

    private Castle(Application application, CastleConfiguration castleConfiguration) {
        setup(application, castleConfiguration);
    }

    private void setup(Application application, CastleConfiguration configuration) {
        Context context = application.getApplicationContext();
        this.storageHelper = new StorageHelper(context);
        this.configuration = configuration;
        this.identifier = storageHelper.getDeviceId();
        this.eventQueue = new EventQueue(context);
        this.application = application;
    }

    private void registerLifeCycleCallbacks(Application application) {
        activityLifecycleCallbacks = new CastleActivityLifecycleCallbacks();
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        componentCallbacks = new CastleComponentCallback();
        application.registerComponentCallbacks(componentCallbacks);

        // Get the current version.
        appVersion = Utils.getApplicationVersion(application);
        appBuild = Utils.getApplicationVersionCode(application);
        appName = Utils.getApplicationName(application);

        // Get the previous recorded version.
        String previousVersion = storageHelper.getVersion();
        int previousBuild = storageHelper.getBuild();

        // Check and track Application Installed or Application Updated.
        if (previousBuild == -1) {
            track("Application Installed");
        } else if (appBuild != previousBuild) {
            track("Application Updated");
        }

        // Track Application Opened.
        track("Application Opened");

        flush();

        // Update the recorded version.
        storageHelper.setVersion(appVersion);
        storageHelper.setBuild(appBuild);
    }

    /**
     * Configure Castle using the provided configuration
     * @param application Application instance
     * @param configuration CastleConfiguration
     */
    public static void configure(Application application, CastleConfiguration configuration) {
        if (instance == null) {
            if (configuration.publishableKey() == null || !configuration.publishableKey().startsWith("pk_")) {
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
     * Track event with a specified name
     * @param event Event name
     */
    protected static void track(String event) {
        if (event == null || event.isEmpty()) {
            return;
        }
        track(new Event(event));
    }

    protected static void track(Event event) {
        CastleLogger.d("Tracking event " + Utils.getGsonInstance().toJson(event));
        instance.eventQueue.add(event);
        if (instance.eventQueue.needsFlush()) {
            flush();
        }
    }

    /**
     * Track identify event with specified user identity. User identity will be persisted. A call to identify or reset will clear the stored user identity.
     * @param userId user id
     */
    public static void identify(String userId) {
        identify(userId, new HashMap<>());
    }

    /**
     * Track identify event with specified user identity. User identity will be persisted. A call to identify or reset will clear the stored user identity. Provided user traits will be included in the identify event sent to the Castle API.
     * @param userId user id
     * @param traits user traits
     */
    public static void identify(String userId, Map<String, String> traits) {
        if (userId == null || userId.isEmpty() || traits == null) {
            return;
        }

        // Log warning if identify is called without secure mode signature set.
        if (!Castle.secureModeEnabled()) {
            CastleLogger.w("Identify called without secure mode signature set. If secure mode is enabled in Castle and identify is called before secure, the identify event will be discarded.");
        }

        Castle.userId(userId);
        track(new IdentifyEvent(userId, traits));
        flush();
    }

    /**
     * Set user id
     * @param userId  user id
     */
    private static void userId(String userId) {
        instance.storageHelper.setUserId(userId);
    }

    /**
     * Get user id from last identify call, returns null if not set
     * @return user id
     */
    public static String userId() {
        return instance.storageHelper.getUserId();
    }

    /**
     * Reset any stored user information and flush the event queue
     */
    public static void reset() {
        Castle.flush();
        Castle.userId(null);
        Castle.userSignature(null);
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
     * Set signature to use for Secure Mode
     * @param signature Signature sent to Castle to verify secure mode
     */
    public static void secure(String signature) {
        if (signature == null || signature.isEmpty()) {
            return;
        }
        Castle.userSignature(signature);
    }

    /**
     * Check if a signature is set and secure mode enabled
     * @return True if signature is set
     */
    public static boolean secureModeEnabled() {
        return Castle.userSignature() != null;
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
     * Get identifier if set, otherwise returns null
     * @return identifier
     */
    public static String clientId() {
        return instance.identifier;
    }

    /**
     * Get the user signature if set, otherwise returns null
     * @return signature
     */
    public static String userSignature() {
        return instance.storageHelper.getUserSignature();
    }

    /**
     * Set the user signature for secure mode
     * @param signature The signature to be used for secure mode
     */
    public static void userSignature(String signature) {
        instance.storageHelper.setUserSignature(signature);
    }

    /**
     * Get configuration
     * @return configuration
     */
    public static CastleConfiguration configuration() {
        return instance.configuration;
    }

    /**
     * Force a flush of the batch event queue, even if the flush limit hasn’t been reached
     */
    public static void flush() {
        try {
            instance.eventQueue.flush();
        } catch (IOException exception) {
            CastleLogger.e("Unable to flush queue", exception);
        }
    }

    /**
     * Force a flush if needed for a specific url, flushes if url is whitelisted
     */
    public static boolean flushIfNeeded(String url) {
        // Flush if request to whitelisted url
        if (isUrlWhiteListed(url)) {
            flush();
            return true;
        }
        return false;
    }

    /**
     * Get Castle headers for a specific url, returns non-empty when url is whitelisted
     */
    public static Map<String, String> headers(String url) {
        Map<String, String> headers = new HashMap<>();

        if (isUrlWhiteListed(url)) {
            headers.put(clientIdHeaderName, Castle.clientId());
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
     * Determine if a given url is whitelisted
     * @return url whitelist status
     */
    static boolean isUrlWhiteListed(String urlString) {
        try {
            URL url = new URL(urlString);
            String baseUrl = url.getProtocol() + "://" + url.getHost() + "/";

            if (Castle.configuration().baseURLWhiteList() != null && !Castle.configuration().baseURLWhiteList().isEmpty()) {
                if (Castle.configuration().baseURLWhiteList().contains(baseUrl)) {
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
     * @return User agent string in format application name/version (versionCode) (Device name; Android version; Castle library version)
     */
    public static String userAgent() {
        return Utils.sanitizeHeader(String.format(Locale.US, "%s/%s (%d) (%s %s; Android %s; Castle %s)", instance.appName, instance.appVersion, instance.appBuild, Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE, BuildConfig.VERSION_NAME));
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

    /**
     * Get context with device information
     * @return Context with current device information
     */
    public static io.castle.android.api.model.Context createContext() {
        return io.castle.android.api.model.Context.create(instance.application.getApplicationContext());
    }
}
