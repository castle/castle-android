/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import java.util.List;

public class CastleConfiguration {
    private boolean debugLoggingEnabled = false;

    private int flushLimit = 20;
    private int maxQueueLimit = 1000;

    private String publishableKey;
    private boolean screenTrackingEnabled = true;

    private List<String> baseURLWhiteList;

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
        this.debugLoggingEnabled = builder.debugLoggingEnabled;
        this.flushLimit = builder.flushLimit();
        this.maxQueueLimit = builder.maxQueueLimit();
        this.publishableKey = builder.publishableKey();
        this.screenTrackingEnabled = builder.screenTrackingEnabled();
        this.baseURLWhiteList = builder.baseURLWhiteList();
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
     * Builder used for creating a configuration
     */
    public static final class Builder {
        private boolean debugLoggingEnabled;
        private int flushLimit;
        private int maxQueueLimit;
        private String publishableKey;
        private boolean screenTrackingEnabled;
        private List<String> baseURLWhiteList;

        /**
         * Create builder with defaults
         */
        public Builder() {
            debugLoggingEnabled = false;
            flushLimit = 20;
            maxQueueLimit = 1000;
            screenTrackingEnabled = true;
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
         * Build configuration from builder
         * @return Configuration
         */
        public CastleConfiguration build() {
            return new CastleConfiguration(this);
        }
    }
}
