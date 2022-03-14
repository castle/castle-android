/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import android.app.Application;
import android.os.Build;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

@RunWith(AndroidJUnit4.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
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
        ArrayList<String> baseURLAllowList = new ArrayList<>();
        baseURLAllowList.add("https://google.com/");

        CastleConfiguration configuration = new CastleConfiguration.Builder()
                .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                .screenTrackingEnabled(true)
                .debugLoggingEnabled(true)
                .flushLimit(2)
                .baseURLAllowList(baseURLAllowList)
                .maxQueueLimit(100)
                .build();

        Assert.assertTrue(configuration.screenTrackingEnabled());
        Assert.assertTrue(configuration.debugLoggingEnabled());
        Assert.assertEquals(2, configuration.flushLimit());
        Assert.assertEquals(1, configuration.baseURLAllowList().size());
        Assert.assertEquals("https://google.com/", configuration.baseURLAllowList().get(0));
        Assert.assertEquals(100, configuration.maxQueueLimit());
        Assert.assertEquals("https://m.castle.io/v1/", configuration.baseUrl());

        // Setup Castle SDK with provided configuration
        Castle.configure(application, configuration);

        Assert.assertEquals(2, Castle.configuration().flushLimit());
        Assert.assertEquals(1, Castle.configuration().baseURLAllowList().size());
        Assert.assertEquals("https://google.com/", Castle.configuration().baseURLAllowList().get(0));
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

        Castle.configure(application, "pk_SE5aTeotKZpDEn8kurzBYquRZyy22222", configuration);

        Assert.assertEquals("pk_SE5aTeotKZpDEn8kurzBYquRZyy22222", Castle.publishableKey());

        // Destroy current instance
        Castle.destroy(application);
    }

    @After
    public void after() {
        Castle.destroy(application);
    }
}
