/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import static org.awaitility.Awaitility.await;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.Manifest;
import android.app.Application;
import android.os.Build;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import io.castle.android.api.model.Event;
import io.castle.android.api.model.ScreenEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RunWith(AndroidJUnit4.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class CastleTest {
    private static final long AWAIT_TIMEOUT = 5 * 60;

    @Rule
    public ActivityTestRule<TestActivity> rule  = new ActivityTestRule<>(TestActivity.class);

    @Rule
    public GrantPermissionRule runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_NETWORK_STATE);

    private Application application;
    private OkHttpClient client;

    @Before
    public void setup() {
        application = rule.getActivity().getApplication();

        rule.getActivity().setTitle("TestActivityTitle");

        ArrayList<String> baseURLAllowList = new ArrayList<>();
        baseURLAllowList.add("https://google.com/");

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
        Assert.assertEquals(Castle.userJwt(), userJwt);
    }

    @Test
    public void testflushIfNeeded() {
        // Make sure flush is done for allowlisted base url
        boolean flushed = Castle.flushIfNeeded("https://google.com/");

        // FlushIfNeeded returns true if url is allowlisted and flush() is called
        Assert.assertTrue(flushed);

        // Make sure flush is NOT done for non allowlisted base url
        flushed = Castle.flushIfNeeded("https://test.com");

        // FlushIfNeeded returns fasle if url is not allowlisted
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
        Assert.assertEquals(screenEvent.getName(), "Main");
        Assert.assertEquals(screenEvent.getType(), Event.EVENT_TYPE_SCREEN);

        screenEvent = new ScreenEvent(rule.getActivity());
        Assert.assertEquals(screenEvent.getName(), "TestActivityTitle");
        Assert.assertEquals(screenEvent.getType(), Event.EVENT_TYPE_SCREEN);

        // Test null activity title
        rule.getActivity().setTitle(null);

        screenEvent = new ScreenEvent(rule.getActivity());
        Assert.assertEquals(screenEvent.getName(), "TestActivity");
        Assert.assertEquals(screenEvent.getType(), Event.EVENT_TYPE_SCREEN);

        count = Castle.queueSize();
        Castle.screen("Main");

        // Wait until event is added in background thread
        await().atMost(AWAIT_TIMEOUT, SECONDS).until(eventIsAdded(count));

        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        count = Castle.queueSize();
        Castle.screen(rule.getActivity());

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
        Map<String, String> headers = Castle.headers("https://google.com/test");
        Assert.assertNotNull(headers);
        Assert.assertTrue(!headers.isEmpty());
        Assert.assertTrue(headers.containsKey(Castle.requestTokenHeaderName));
    }

    @Test
    public void testRequestInterceptor() throws IOException {
        Request request = new Request.Builder()
                .url("https://google.com/test")
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertNotNull(response.request().header(Castle.requestTokenHeaderName));

        request = new Request.Builder()
                .url("https://example.com/test")
                .build();

        response = client.newCall(request).execute();
        Assert.assertEquals(null, response.request().header(Castle.requestTokenHeaderName));
    }

    @Test
    public void testAllowlist() {
        Assert.assertFalse(Castle.isUrlAllowlisted("invalid url"));
    }

    @Test
    @Config(manifest = "AndroidManifest.xml")
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
