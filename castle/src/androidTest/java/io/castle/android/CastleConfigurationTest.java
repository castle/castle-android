package io.castle.android;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Copyright (c) 2017 Castle
 */
@RunWith(AndroidJUnit4.class)
public class CastleConfigurationTest {
    private Application application;

    @Before
    public void setup() {
        application = (Application) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

    @Test
    public void testConfiguration() {
        ArrayList<String> baseUrlWhiteList = new ArrayList<>();
        baseUrlWhiteList.add("https://google.com/");

        CastleConfiguration configuration = new CastleConfiguration(application);
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
        Castle.configure(application, configuration);

        Assert.assertEquals(1, Castle.headers("https://google.com/test").size());

        while (Castle.isFlushingQueue()) {

        }

        // Destroy current instance
        Castle.destroy(application);

        Castle.configure(application, "pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ");
        Assert.assertEquals("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ", Castle.publishableKey());

        // Destroy current instance
        Castle.destroy(application);

        Castle.configure(application, "pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ", configuration);
        Assert.assertEquals("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ", Castle.publishableKey());

        // Destroy current instance
        Castle.destroy(application);

        // Setup Castle SDK with invalid publishableKey
        configuration = new CastleConfiguration(application);
        configuration.publishableKey("sk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ");
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
    }
}
