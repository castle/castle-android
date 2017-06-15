package io.castle.android;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.castle.android.api.model.Event;
import io.castle.android.api.model.ScreenEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (c) 2017 Castle
 */
@RunWith(AndroidJUnit4.class)
public class ConfigurationTest {
    private Application application;

    @Before
    public void setup() {
        application = (Application) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

    @Test
    public void testConfiguration() {
        ArrayList<String> baseUrlWhiteList = new ArrayList<>();
        baseUrlWhiteList.add("https://google.com/");

        Configuration configuration = new Configuration(application);
        configuration.publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ");
        configuration.screenTrackingEnabled(true);
        configuration.debugLoggingEnabled(true);
        configuration.flushLimit(10);
        configuration.baseURLWhiteList(baseUrlWhiteList);
        configuration.maxQueueLimit(1000);

        Assert.assertTrue(configuration.screenTrackingEnabled());
        Assert.assertTrue(configuration.debugLoggingEnabled());
        Assert.assertEquals(10, configuration.flushLimit());
        Assert.assertEquals(1, configuration.baseURLWhiteList().size());
        Assert.assertEquals("https://google.com/", configuration.baseURLWhiteList().get(0));
        Assert.assertEquals(1000, configuration.maxQueueLimit());

        // Setup Castle SDK with provided configuration
        Castle.setupWithConfiguration(application, configuration);

        Assert.assertEquals(1, Castle.headers("https://google.com/test").size());

        while (Castle.isFlushingQueue()) {

        }

        // Destroy current instance
        Castle.destroy(application);

        // Setup Castle SDK with invalid publishableKey
        configuration = new Configuration(application);
        configuration.publishableKey("sk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ");
        try {
            Castle.setupWithConfiguration(application, configuration);
            Assert.fail("Should have thrown RuntimeException exception");
        } catch (RuntimeException e) {
            // Success
        }

        // Destroy current instance
        Castle.destroy(application);

        // Test version upgrade
        new StorageHelper(application).setBuild(0);

        Castle.setupWithDefaultConfiguration(application);
    }
}
