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

import io.castle.android.api.model.Custom;
import io.castle.android.api.model.Model;
import io.castle.android.api.model.ScreenEvent;
import io.castle.android.api.model.User;
import io.castle.android.api.model.UserJwt;
import io.castle.highwind.android.Highwind;
import io.castle.android.queue.EventQueue;

/**
 * This class is the main entry point for using the Castle SDK and provides methods for tracking events, screen views, manual flushing of the event queue, allowlisting behaviour and resetting.
 */
public class Castle {
    public static final String clientIdHeaderName = "X-Castle-Client-Id";

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

    public static String encodeEvent(Model event) {
        if (event instanceof ScreenEvent) {
            return instance.highwind.encodeScreenEvent(event.getToken(), Utils.getGsonInstance().toJson(event), false);
        }
        return instance.highwind.encodeCustomEvent(event.getToken(), Utils.getGsonInstance().toJson(event), false);
    }

    public static String encodeUser(User user) {
        return instance.highwind.encodeUserPayloadSet(Utils.getGsonInstance().toJson(user), false);
    }

    public static String encodeUser(String userJwt) {
        return instance.highwind.encodeUserPayloadSet(Utils.getGsonInstance().toJson(new UserJwt(userJwt)), false);
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
        this.highwind = new Highwind(context, BuildConfig.VERSION_NAME, storageHelper.getDeviceId(), buildUserAgent(), configuration.publishableKey());
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
     * Track event with a specified name
     * @param event Event name
     */
    public static void track(String event) {
        if (event == null || event.isEmpty()) {
            return;
        }
        track(new Custom(event));
    }

    private static void track(Model event) {
        instance.eventQueue.add(event);
    }

    /**
     * Track identify event with specified user identity. User identity will be persisted. A call to identify or reset will clear the stored user identity.
     * @param userId user id
     */
    public static void identify(String userId, String signature) {
        identify(userId, signature, new HashMap<>());
    }

    /**
     * Track identify event with specified user identity. User identity will be persisted. A call to identify or reset will clear the stored user identity. Provided user properties will be included in the identify event sent to the Castle API.
     * @param identifier user identifier
     * @param properties user properties
     */
    public static void identify(String identifier, String signature, Map<String, Object> properties) {
        if (identifier == null || identifier.isEmpty() || properties == null) {
            CastleLogger.e("No user id provided. Will cancel identify operation.");
            return;
        }

        // Log warning if identify is called without secure mode signature.
        if (signature == null || signature.isEmpty()) {
            CastleLogger.w("Identify called without secure mode signature set. Will cancel identify operation");
        }

        User user = User.userWithId(identifier, signature, properties);
        Castle.user(user);
    }

    /**
     * Set user id
     * @param user  user
     */
    private static void user(User user) {
        instance.storageHelper.setUser(user);
    }

    /**
     * Get user id from last identify call, returns null if not set
     * @return user id
     */
    public static User user() {
        return instance.storageHelper.getUser();
    }

    /**
     * Reset any stored user information and flush the event queue
     */
    public static void reset() {
        Castle.flush();
        Castle.user(null);
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
     * Force a flush of the batch event queue, even if the flush limit hasn’t been reached
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
            headers.put(clientIdHeaderName, Castle.createRequestToken());
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
     * Get custom user agent used for requests sent to Castle API
     * @return User agent string in format application name/version (versionCode) (Castle library version; Android version; Device name)
     */
    public static String userAgent() {
        return instance.buildUserAgent();
    }

    private String buildUserAgent() {
        return Utils.sanitizeHeader(String.format(Locale.US, "%s/%s (%d) (Castle %s; Android %s; %s %s)", appName, appVersion, appBuild, BuildConfig.VERSION_NAME, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.MODEL));
    }

    private void unregisterLifeCycleCallbacks(Application application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    private void unregisterComponentCallbacks(Application application) {
        application.unregisterComponentCallbacks(componentCallbacks);
    }
}
