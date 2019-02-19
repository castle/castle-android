/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import android.Manifest;
import android.app.Application;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
public class CastleTest {
    @Rule
    public ActivityTestRule<TestActivity> rule  = new ActivityTestRule<>(TestActivity.class);

    @Rule
    public GrantPermissionRule runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_NETWORK_STATE);

    private Application application;
    private OkHttpClient client;

    @Before
    public void setup() {
        application = rule.getActivity().getApplication();

        ArrayList<String> baseUrlWhiteList = new ArrayList<>();
        baseUrlWhiteList.add("https://google.com/");

        Castle.configure(application, new CastleConfiguration.Builder()
                .publishableKey("pk_SE5aTeotKZpDEn8kurzBYquRZy")
                .screenTrackingEnabled(true)
                .baseURLWhiteList(baseUrlWhiteList)
                .build());

        client = new OkHttpClient.Builder()
                .addInterceptor(Castle.castleInterceptor())
                .build();
    }

    @Test
    public void testDeviceIdentifier() {
        // Check device ID
        Assert.assertNotNull(Castle.clientId());
    }

    @Test
    public void testUserIdPersistance() {
        // Make sure the user id is persisted correctly after identify
        Castle.identify("thisisatestuser");

        // Check that the stored identity is the same as the identity we tracked
        Assert.assertEquals(Castle.userId(), "thisisatestuser");
    }

    @Test
    public void testflushIfNeeded() {
        // Make sure flush is done for whitelisted base url
        boolean flushed = Castle.flushIfNeeded("https://google.com/");

        // FlushIfNeeded returns true if url is whitelisted and flush() is called
        Assert.assertTrue(flushed);

        // Make sure flush is NOT done for non whitelisted base url
        flushed = Castle.flushIfNeeded("https://test.com");

        // FlushIfNeeded returns fasle if url is not whitelisted
        Assert.assertFalse(flushed);
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

        count = Castle.queueSize();
        Castle.track("Event");
        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        count = Castle.queueSize();
        Castle.track("Event", new HashMap<String, String>());
        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        count = Castle.queueSize();
        Castle.track("Event", null);
        newCount = Castle.queueSize();
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

        count = Castle.queueSize();
        Castle.screen("Main");
        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        count = Castle.queueSize();
        Castle.screen("Main", new HashMap<String, String>());
        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        count = Castle.queueSize();
        Castle.screen(rule.getActivity());
        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        count = Castle.queueSize();
        Castle.identify("testuser1");
        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        count = Castle.queueSize();
        Castle.identify("testuser1", new HashMap<String, String>());
        newCount = Castle.queueSize();
        Assert.assertEquals(count + 1, newCount);

        Castle.flush();
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
        Assert.assertTrue(headers.containsKey(Castle.clientIdHeaderName));
        Assert.assertEquals(headers.get(Castle.clientIdHeaderName), Castle.clientId());
    }

    @Test
    public void testRequestInterceptor() throws IOException {
        Request request = new Request.Builder()
                .url("https://google.com/test")
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(Castle.clientId(), response.request().header(Castle.clientIdHeaderName));

        request = new Request.Builder()
                .url("https://example.com/test")
                .build();

        response = client.newCall(request).execute();
        Assert.assertEquals(null, response.request().header(Castle.clientIdHeaderName));
    }

    @Test
    public void testWhiteList() {
        Assert.assertFalse(Castle.isUrlWhiteListed("invalid url"));
    }

    @Test
    public void testSecureMode() {

        Castle.secure(null);

        Assert.assertFalse(Castle.secureModeEnabled());

        Castle.secure("");

        Assert.assertFalse(Castle.secureModeEnabled());

        String signature = "944d7d6c5187cafac297785bbf6de0136a2e10f31788e92b2822f5cfd407fa52";

        Castle.secure(signature);

        Assert.assertTrue(Castle.secureModeEnabled());
    }

    @Test
    @Config(manifest = "AndroidManifest.xml")
    public void testUserAgent() {
        String regex = "[a-zA-Z0-9\\s._-]+/[0-9]+\\.[0-9]+\\.?[0-9]* \\([a-zA-Z0-9-_.]+\\) \\([a-zA-Z0-9\\s]+; Android [0-9]+\\.?[0-9]*; Castle [0-9]+\\.[0-9]+\\.?[0-9]*\\)";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(Castle.userAgent());

        Assert.assertTrue(matcher.matches());
    }

    @After
    public void after() {
        Castle.destroy(application);
    }
}
