package io.castle.android;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import io.castle.android.api.model.Event;
import io.castle.android.api.model.ScreenEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Copyright (c) 2017 Castle
 */
@RunWith(AndroidJUnit4.class)
public class CastleTest {
    @Rule
    public MockWebServer server = new MockWebServer();

    private Application application;
    private OkHttpClient client;

    @Before
    public void setup() {
        application = (Application) InstrumentationRegistry.getTargetContext().getApplicationContext();

        ArrayList<String> baseUrlWhiteList = new ArrayList<>();
        baseUrlWhiteList.add("https://google.com/");

        Configuration configuration = new Configuration(application);
        configuration.publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ");
        configuration.baseURLWhiteList(baseUrlWhiteList);

        Castle.setupWithConfiguration(application, configuration);

        client = new OkHttpClient.Builder()
                .addInterceptor(Castle.castleInterceptor())
                .build();
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

        Assert.assertTrue(configuration.screenTrackingEnabled());
        Assert.assertTrue(configuration.debugLoggingEnabled());
        Assert.assertEquals(10, configuration.flushLimit());
        Assert.assertEquals(1, configuration.baseURLWhiteList().size());
        Assert.assertEquals("https://google.com/", configuration.baseURLWhiteList().get(0));

        Assert.assertEquals(1, Castle.headers("https://google.com/test").size());

        // Setup Castle SDK with provided configuration
        Castle.setupWithConfiguration(application, configuration);
    }

    @Test
    public void testDeviceIdentifier() {
        // Check device ID
        Assert.assertNotNull(Castle.deviceIdentifier());
    }

    @Test
    public void testUserIdPersistance() {
        // Make sure the user id is persisted correctly after identify
        Castle.identify("thisisatestuser");

        // Check that the stored identity is the same as the identity we tracked
        Assert.assertEquals(Castle.userId(), "thisisatestuser");
    }

    @Test
    public void testReset() {
        Castle.reset();

        // Check to see if the user identity was cleared on reset
        Assert.assertNull(Castle.userId());
    }

    @Test
    public void testTracking() {
        // This should lead to no event being tracked since empty string isn't a valid name
        int count = Castle.queueSize();
        Castle.track("");
        int newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        // This should lead to no event being tracked since empty string isn't a valid name
        count = Castle.queueSize();
        Castle.screen("");
        newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        // This should lead to no event being tracked properties can't be nil
        count = Castle.queueSize();
        Castle.screen("Screen", null);
        newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        // This should lead to no event being tracked since identity can't be an empty string
        count = Castle.queueSize();
        Castle.identify("");
        newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        // This should lead to no event being tracked properties can't be nil
        count = Castle.queueSize();
        Castle.identify("testuser1", null);
        newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        ScreenEvent screenEvent = new ScreenEvent("Main");
        Assert.assertEquals(screenEvent.getEvent(), "Main");
        Assert.assertEquals(screenEvent.getType(), Event.EVENT_TYPE_SCREEN);
    }

    @Test
    public void testDefaultHeaders() {
        Map<String, String> headers = Castle.headers("https://google.com/test");
        Assert.assertNotNull(headers);
        Assert.assertTrue(!headers.isEmpty());
        Assert.assertTrue(headers.containsKey("X-Castle-Mobile-Device-Id"));
        Assert.assertEquals(headers.get("X-Castle-Mobile-Device-Id"), Castle.deviceIdentifier());
    }

    @Test
    public void testRequestInterceptor() throws IOException {
        server.shutdown(); // Accept no connections.

        Request request = new Request.Builder()
                .url("https://google.com/test")
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.request().header("X-Castle-Mobile-Device-Id"), Castle.deviceIdentifier());

        request = new Request.Builder()
                .url("https://example.com/test")
                .build();

        response = client.newCall(request).execute();
        Assert.assertEquals(response.request().header("X-Castle-Mobile-Device-Id"), null);
    }
}
