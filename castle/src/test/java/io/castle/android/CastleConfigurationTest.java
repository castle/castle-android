/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import android.app.Application;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

@RunWith(AndroidJUnit4.class)
public class CastleConfigurationTest {
    @Rule
    public ActivityTestRule<TestActivity> rule  = new ActivityTestRule<>(TestActivity.class);

    private Application application;

    @Before
    public void setup() {
        application = rule.getActivity().getApplication();
    }

    @Test
    public void testConfiguration() {
        ArrayList<String> baseURLAllowlist = new ArrayList<>();
        baseURLAllowlist.add("https://google.com/");

        CastleConfiguration configuration = new CastleConfiguration.Builder()
                .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                .screenTrackingEnabled(true)
                .debugLoggingEnabled(true)
                .flushLimit(2)
                .baseURLAllowlist(baseURLAllowlist)
                .maxQueueLimit(100)
                .build();

        Assert.assertTrue(configuration.screenTrackingEnabled());
        Assert.assertTrue(configuration.debugLoggingEnabled());
        Assert.assertEquals(2, configuration.flushLimit());
        Assert.assertEquals(1, configuration.baseURLAllowlist().size());
        Assert.assertEquals("https://google.com/", configuration.baseURLAllowlist().get(0));
        Assert.assertEquals(100, configuration.maxQueueLimit());
        Assert.assertFalse(configuration.useCloudflareApp());
        Assert.assertEquals("https://api.castle.io/v1/", configuration.baseUrl());

        // Setup Castle SDK with provided configuration
        Castle.configure(application, configuration);

        Assert.assertEquals(2, Castle.configuration().flushLimit());
        Assert.assertEquals(1, Castle.configuration().baseURLAllowlist().size());
        Assert.assertEquals("https://google.com/", Castle.configuration().baseURLAllowlist().get(0));
        Assert.assertEquals(100, Castle.configuration().maxQueueLimit());

        Assert.assertEquals(1, Castle.headers("https://google.com/test").size());

        // Try and set up SDK with new configuration while already configured
        configuration = new CastleConfiguration.Builder()
                .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                .build();

        Castle.configure(application, configuration);

        Assert.assertEquals(100, Castle.configuration().maxQueueLimit());

        // Destroy current instance
        Castle.destroy(application);

        Castle.configure(application, "pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ");
        Assert.assertEquals("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ", Castle.publishableKey());

        // Destroy current instance
        Castle.destroy(application);

        // Setup Castle SDK with invalid publishableKey
        configuration = new CastleConfiguration.Builder()
                .publishableKey("sk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                .build();
        try {
            Castle.configure(application, configuration);
            Assert.fail("Should have thrown RuntimeException exception");
        } catch (RuntimeException e) {
            // Success
        }

        // Destroy current instance
        Castle.destroy(application);

        // Test version upgrade
        new StorageHelper(application).setBuild(0);

        Castle.configure(application);

        // Destroy current instance
        Castle.destroy(application);

        // Try and set up SDK with new configuration while already configured
        configuration = new CastleConfiguration.Builder()
                .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                .build();

        Castle.configure(application, "pk_123", configuration);

        Assert.assertEquals("pk_123", Castle.publishableKey());

        // Destroy current instance
        Castle.destroy(application);

        // Test cloudflare logic
        try {
            configuration = new CastleConfiguration.Builder()
                    .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                    .useCloudflareApp(true)
                    .build();
            Assert.fail("Should have thrown RuntimeException exception");
        } catch (RuntimeException e) {
            // Success
        }

        configuration = new CastleConfiguration.Builder()
                .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                .useCloudflareApp(true)
                .apiDomain("example.com")
                .build();

        Assert.assertTrue(configuration.useCloudflareApp());
        Assert.assertEquals("example.com", configuration.apiDomain());
        Assert.assertEquals("https://example.com/v1/c/mobile/", configuration.baseUrl());

        Castle.configure(application, configuration);

        Assert.assertEquals("https://example.com/v1/c/mobile/", Castle.baseUrl());

        configuration = new CastleConfiguration.Builder()
                .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                .useCloudflareApp(true)
                .apiDomain("example.com")
                .apiPath("v1/test/")
                .build();

        Assert.assertTrue(configuration.useCloudflareApp());
        Assert.assertEquals("example.com", configuration.apiDomain());
        Assert.assertEquals("https://example.com/v1/test/", configuration.baseUrl());


    }

    @After
    public void after() {
        Castle.destroy(application);
    }
}
