/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import java.util.List;

public class CastleConfiguration {

    private static final boolean DEFAULT_DEBUG_LOGGING_ENABLED = false;
    private static final int DEFAULT_FLUSH_LIMIT = 20;
    private static final int DEFAULT_MAX_QUEUE_LIMIT = 1000;
    private static final boolean DEFAULT_SCREEN_TRACKING_ENABLED = true;
    private static final boolean DEFAULT_USE_CLOUDFLARE_APP = false;
    private static final String DEFAULT_API_DOMAIN = "api.castle.io";
    private static final String CASTLE_API_PATH = "v1/";
    private static final String CLOUDFLARE_API_PATH = "v1/c/mobile/";

    private boolean debugLoggingEnabled;
    private int flushLimit;
    private int maxQueueLimit;
    private boolean screenTrackingEnabled;
    private List<String> baseURLWhiteList;
    private String publishableKey;
    private boolean useCloudflareApp;
    private String apiDomain;
    private String apiPath;

    /**
     * Create default configuration
     */
    public CastleConfiguration() {
        this(new Builder());
    }

    /**
     * Create configuration with builder
     */
    private CastleConfiguration(Builder builder) {
        this.debugLoggingEnabled = builder.debugLoggingEnabled();
        this.flushLimit = builder.flushLimit();
        this.maxQueueLimit = builder.maxQueueLimit();
        this.publishableKey = builder.publishableKey();
        this.screenTrackingEnabled = builder.screenTrackingEnabled();
        this.baseURLWhiteList = builder.baseURLWhiteList();
        this.useCloudflareApp = builder.useCloudflareApp();
        this.apiDomain = builder.apiDomain();
        this.apiPath = builder.apiPath();

        if (useCloudflareApp && apiDomain.equals(DEFAULT_API_DOMAIN)) {
            throw new RuntimeException("You must set a API domain if useCloudflare app is enabled.");
        }
    }

    /**
     * Get list of whitelisted urls
     * @return List of whitelisted urls
     */
    public List<String> baseURLWhiteList() {
        return baseURLWhiteList;
    }

    /**
     * Get debug logging enabled
     * @return Debug logging enabled
     */
    public boolean debugLoggingEnabled() {
        return debugLoggingEnabled;
    }

    /**
     * Get flushlimit
     * @return Flushlimit
     */
    public int flushLimit() {
        return flushLimit;
    }

    /**
     * Get max queue size limit
     * @return Max queue size limit
     */
    public int maxQueueLimit() {
        return maxQueueLimit;
    }

    /**
     * Get publishable key
     * @return Publishable key
     */
    public String publishableKey() {
        return publishableKey;
    }

    /**
     * Get screen tracking enabled
     * @return Screen tracking enabled
     */
    public boolean screenTrackingEnabled() {
        return screenTrackingEnabled;
    }

    /**
     * Get use cloudflare app enabled
     * @return Use Cloudflare app enabled
     */
    public boolean useCloudflareApp() { return useCloudflareApp; }

    /**
     * Get API domain
     * @return API domain
     */
    public String apiDomain() { return apiDomain; }

    /**
     * Get API path
     * @return API path
     */
    public String apiPath() {
        if (useCloudflareApp) {
            if (apiPath != null) {
                return apiPath;
            } else {
                return CLOUDFLARE_API_PATH;
            }
        }
        return CASTLE_API_PATH;
    }

    public String baseUrl() {
        if (useCloudflareApp) {
            return String.format("https://%s/%s", apiDomain(), apiPath());
        }
        return String.format("https://%s/%s", DEFAULT_API_DOMAIN, apiPath());
    }

    /**
     * Builder used for creating a configuration
     */
    public static final class Builder {
        private boolean debugLoggingEnabled;
        private int flushLimit;
        private int maxQueueLimit;
        private String publishableKey;
        private boolean screenTrackingEnabled;
        private List<String> baseURLWhiteList;
        private boolean useCloudflareApp;
        private String apiDomain;
        private String apiPath;

        /**
         * Create builder with defaults
         */
        public Builder() {
            debugLoggingEnabled = DEFAULT_DEBUG_LOGGING_ENABLED;
            flushLimit = DEFAULT_FLUSH_LIMIT;
            maxQueueLimit = DEFAULT_MAX_QUEUE_LIMIT;
            screenTrackingEnabled = DEFAULT_SCREEN_TRACKING_ENABLED;
            useCloudflareApp = DEFAULT_USE_CLOUDFLARE_APP;
            apiDomain = DEFAULT_API_DOMAIN;
            apiPath = null;
        }

        /**
         * Create builder with values from a configuration
         */
        public Builder(CastleConfiguration configuration) {
            debugLoggingEnabled = configuration.debugLoggingEnabled();
            flushLimit = configuration.flushLimit();
            maxQueueLimit = configuration.maxQueueLimit();
            publishableKey = configuration.publishableKey();
            screenTrackingEnabled = configuration.screenTrackingEnabled();
            baseURLWhiteList = configuration.baseURLWhiteList();
            useCloudflareApp = configuration.useCloudflareApp();
            apiDomain = configuration.apiDomain();
            apiPath = configuration.apiPath();
        }

        /**
         * Set whitelist
         * @param baseURLWhiteList
         * @return Builder
         */
        public Builder baseURLWhiteList(List<String> baseURLWhiteList) {
            this.baseURLWhiteList = baseURLWhiteList;

            return this;
        }

        /**
         * Get whitelist
         * @return Whitelist
         */
        public List<String> baseURLWhiteList() {
            return baseURLWhiteList;
        }

        /**
         * Set debug logging
         * @param enabled
         * @return Builder
         */
        public Builder debugLoggingEnabled(boolean enabled) {
            this.debugLoggingEnabled = enabled;

            return this;
        }

        /**
         * Get debug logging
         * @return Debug logging enabled
         */
        public boolean debugLoggingEnabled() {
            return debugLoggingEnabled;
        }

        /**
         * Get flush limit
         * @return Flush limit
         */
        public int flushLimit() {
            return flushLimit;
        }

        /**
         * Set flush limit
         * @param flushLimit
         * @return Builder
         */
        public Builder flushLimit(int flushLimit) {
            this.flushLimit = flushLimit;

            return this;
        }

        /**
         * Get max queue limit
         * @return Max queue limit
         */
        public int maxQueueLimit() {
            return maxQueueLimit;
        }

        /**
         * Set max queue limit
         * @param maxQueueLimit
         * @return Builder
         */
        public Builder maxQueueLimit(int maxQueueLimit) {
            this.maxQueueLimit = maxQueueLimit;

            return this;
        }

        /**
         * Set publishable key
         * @param publishableKey
         * @return Builder
         */
        public Builder publishableKey(String publishableKey) {
            this.publishableKey = publishableKey;

            return this;
        }

        /**
         * Get publishable key
         * @return Publishable key
         */
        public String publishableKey() {
            return publishableKey;
        }

        /**
         * Set screen tracking enabled
         * @param enabled
         * @return Builder
         */
        public Builder screenTrackingEnabled(boolean enabled) {
            this.screenTrackingEnabled = enabled;

            return this;
        }

        /**
         * Get screen tracking enabled
         * @return Screen tracking enabled
         */
        public boolean screenTrackingEnabled() {
            return screenTrackingEnabled;
        }

        /**
         * Set use cloudflare app enabled
         * @param enabled
         * @return Builder
         */
        public Builder useCloudflareApp(boolean enabled) {
            this.useCloudflareApp = enabled;

            return this;
        }

        /**
         * Get use cloudflare app enabled
         * @return Use Cloudflare app enabled
         */
        public boolean useCloudflareApp() { return useCloudflareApp; }

        /**
         * Set apiDomain
         * @param apiDomain
         * @return Builder
         */
        public Builder apiDomain(String apiDomain) {
            this.apiDomain = apiDomain;

            return this;
        }

        /**
         * Get API path
         * @return API path
         */
        public String apiPath() { return apiPath; }

        /**
         * Set apiPath
         * @param apiPath
         * @return Builder
         */
        public Builder apiPath(String apiPath) {
            this.apiPath = apiPath;

            return this;
        }

        /**
         * Get API domain
         * @return API domain
         */
        public String apiDomain() { return apiDomain; }

        /**
         * Build configuration from builder
         * @return Configuration
         */
        public CastleConfiguration build() {
            return new CastleConfiguration(this);
        }
    }
}
