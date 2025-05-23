/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import static org.awaitility.Awaitility.await;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.os.Build;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import io.castle.android.api.model.Event;
import io.castle.android.api.model.ScreenEvent;
import io.castle.android.testsupport.TestActivity;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class CastleTest {
    private static final long AWAIT_TIMEOUT = 5 * 60;

    @Rule
    public GrantPermissionRule runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_NETWORK_STATE);

    private Application application;
    private Activity activity;
    private OkHttpClient client;
    private MockWebServer server;
    private String baseUrl;
    private ArrayList<String> baseURLAllowList;

    @Before
    public void setup() {
        application = ApplicationProvider.getApplicationContext();

        server = new MockWebServer();

        baseUrl = server.url("/").toString().replace(":" + server.getPort(), "");

        activity = Robolectric.buildActivity(TestActivity.class)
                .create()  // Creates the activity
                .start()   // Starts the activity
                .resume()  // Resumes the activity to make it interactive
                .get();

        activity.setTitle("TestActivityTitle");

        baseURLAllowList = new ArrayList<>();
        baseURLAllowList.add(baseUrl);

        configure(baseURLAllowList);
    }

    private void configure(ArrayList<String> baseURLAllowList) {
        Castle.destroy(application);

        Castle.configure(application, new CastleConfiguration.Builder()
                .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZyy21fvZ")
                .screenTrackingEnabled(true)
                .baseURLAllowList(baseURLAllowList)
                .build());

        client = new OkHttpClient.Builder()
                .addInterceptor(Castle.castleInterceptor())
                .build();
    }

    @Test
    public void testDeviceIdentifier() {
        // Check device ID
        Assert.assertNotNull(Castle.createRequestToken());
    }

    @Test
    public void testUserIdPersistance() {
        // Make sure the user id is persisted correctly after identify
        Castle.userJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImVjMjQ0ZjMwLTM0MzItNGJiYy04OGYxLTFlM2ZjMDFiYzFmZSIsImVtYWlsIjoidGVzdEBleGFtcGxlLmNvbSIsInJlZ2lzdGVyZWRfYXQiOiIyMDIyLTAxLTAxVDA5OjA2OjE0LjgwM1oifQ.eAwehcXZDBBrJClaE0bkO9XAr4U3vqKUpyZ-d3SxnH0");

        // Check that the stored identity is the same as the identity we tracked
        String userJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImVjMjQ0ZjMwLTM0MzItNGJiYy04OGYxLTFlM2ZjMDFiYzFmZSIsImVtYWlsIjoidGVzdEBleGFtcGxlLmNvbSIsInJlZ2lzdGVyZWRfYXQiOiIyMDIyLTAxLTAxVDA5OjA2OjE0LjgwM1oifQ.eAwehcXZDBBrJClaE0bkO9XAr4U3vqKUpyZ-d3SxnH0";
        Assert.assertEquals(userJwt, Castle.userJwt());
    }

    @Test
    public void testflushIfNeeded() {

        // Make sure flush is done for allowlisted base url
        boolean flushed = Castle.flushIfNeeded(baseUrl);

        // FlushIfNeeded returns true if url is allowlisted and flush() is called
        Assert.assertTrue(flushed);

        // Make sure flush is NOT done for non allowlisted base url
        flushed = Castle.flushIfNeeded("https://test.com");

        // FlushIfNeeded returns false if url is not allowlisted
        Assert.assertFalse(flushed);
    }

    @Test
    public void testReset() {
        Castle.reset();

        // Check to see if the user identity was cleared on reset
        Assert.assertNull(Castle.userJwt());
    }

    @Test
    public void testTracking() {
        // This should lead to no event being tracked since empty string isn't a valid name
        int count = Castle.queueSize();
        Castle.custom("");
        int newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        Castle.userJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImVjMjQ0ZjMwLTM0MzItNGJiYy04OGYxLTFlM2ZjMDFiYzFmZSIsImVtYWlsIjoidGVzdEBleGFtcGxlLmNvbSIsInJlZ2lzdGVyZWRfYXQiOiIyMDIyLTAxLTAxVDA5OjA2OjE0LjgwM1oifQ.eAwehcXZDBBrJClaE0bkO9XAr4U3vqKUpyZ-d3SxnH0");

        count = Castle.queueSize();
        Castle.custom("Event");

        // Wait until event is added in background thread
        await().atMost(AWAIT_TIMEOUT, SECONDS).until(eventIsAdded(count));

        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        // This should lead to no event being tracked since empty string isn't a valid name
        count = Castle.queueSize();
        Castle.screen("");
        newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        // This should lead to no event being tracked since identity can't be an empty string
        count = Castle.queueSize();
        Castle.userJwt("");
        newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        // This should lead to no event being tracked properties can't be nil
        count = Castle.queueSize();
        Castle.userJwt("testuser1");
        newCount = Castle.queueSize();
        Assert.assertEquals(count, newCount);

        ScreenEvent screenEvent = new ScreenEvent("Main");
        Assert.assertEquals("Main", screenEvent.getName());
        Assert.assertEquals(Event.EVENT_TYPE_SCREEN, screenEvent.getType());

        screenEvent = new ScreenEvent(activity);
        Assert.assertEquals("TestActivityTitle", screenEvent.getName());
        Assert.assertEquals(Event.EVENT_TYPE_SCREEN, screenEvent.getType());

        // Test null activity title
        activity.setTitle(null);

        screenEvent = new ScreenEvent(activity);
        Assert.assertEquals("TestActivity", screenEvent.getName());
        Assert.assertEquals(Event.EVENT_TYPE_SCREEN, screenEvent.getType());

        count = Castle.queueSize();
        Castle.screen("Main");

        // Wait until event is added in background thread
        await().atMost(AWAIT_TIMEOUT, SECONDS).until(eventIsAdded(count));

        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        count = Castle.queueSize();
        Castle.screen(activity);

        // Wait until event is added in background thread
        await().atMost(AWAIT_TIMEOUT, SECONDS).until(eventIsAdded(count));

        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        Castle.flush();
    }

    private Callable<Boolean> eventIsAdded(int size) {
        return () -> Castle.queueSize() == size + 1;
    }

    @Test
    public void testErrorParsing() {
        io.castle.android.api.model.Error error = Utils.getGsonInstance().fromJson("{ \"type\": \"type\", \"message\": \"message\" }", io.castle.android.api.model.Error.class);
        Assert.assertEquals("type", error.getType());
        Assert.assertEquals("message", error.getMessage());
        Assert.assertEquals("type message", error.toString());
    }

    @Test
    public void testDefaultHeaders() {
        Map<String, String> headers = Castle.headers(baseUrl + "test");
        Assert.assertNotNull(headers);
        Assert.assertFalse(headers.isEmpty());
        Assert.assertTrue(headers.containsKey(Castle.requestTokenHeaderName));
    }

    @Test
    public void testRequestInterceptor() throws IOException {
        server.enqueue(new MockResponse().setBody("test"));

        HttpUrl baseUrl = server.url("/test");

        Request request = new Request.Builder()
                .url(baseUrl)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertNotNull(response.request().header(Castle.requestTokenHeaderName));

        // Test that the request token is not added to the request if the url is not allowlisted
        configure(new ArrayList<>());

        server.enqueue(new MockResponse().setBody("test"));

        request = new Request.Builder()
                .url(baseUrl)
                .build();

        response = client.newCall(request).execute();
        Assert.assertNull(response.request().header(Castle.requestTokenHeaderName));

        // Restore configuration
        configure(baseURLAllowList);
    }

    @Test
    public void testAllowlist() {
        Assert.assertFalse(Castle.isUrlAllowlisted("invalid url"));
    }

    @Test
    public void testUserAgent() {
        String regex = "[a-zA-Z0-9\\s._-]+/[0-9]+\\.[0-9]+\\.?[0-9]*(-[a-zA-Z0-9]*)? \\([a-zA-Z0-9-_.]+\\) \\(Castle [0-9]+\\.[0-9]+\\.?[0-9]*(-[a-zA-Z0-9]*)?; Android [0-9]+\\.?[0-9]*\\.?[0-9]*; [a-zA-Z0-9\\s]+\\)";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(Castle.userAgent());

        Assert.assertTrue(matcher.matches());

        matcher = pattern.matcher("io.castle.android.test/1.0 (1) (Castle 1.1.1; Android 9.0; Google Nexus 5x)");

        Assert.assertTrue(matcher.matches());

        matcher = pattern.matcher("io.castle.android.test/1.0-SNAPSHOT (1) (Castle 1.1.1-SNAPSHOT; Android 9.0; Google Nexus 5x)");

        Assert.assertTrue(matcher.matches());

        // Test user agent sanitization
        String result = Utils.sanitizeHeader("[Ţŕéļļö one two]/2020.5.13837-production (13837) (Castle 1.1.2; Android 7.0; motorola Moto G (4))");

        Assert.assertEquals("[ one two]/2020.5.13837-production (13837) (Castle 1.1.2; Android 7.0; motorola Moto G (4))", result);
    }

    @After
    public void after() {
        Castle.destroy(application);
    }
}
