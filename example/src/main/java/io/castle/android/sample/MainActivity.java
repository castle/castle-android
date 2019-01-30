/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.castle.android.Castle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.identify)
    public void onIdentifyClick(Button button) {
        // Identify user with a unique identifier including user traits
        Map<String, String> traits = new HashMap<>();
        traits.put("email", "sebastian@boldsie.com");
        Castle.identify("sebastiansimson", traits);
    }

    @OnClick(R.id.track_screen)
    public void onTrackScreenClick(Button button) {
        // Track a screen view and include some properties
        Map<String, String> properties = new HashMap<>();
        properties.put("deviceOrientation", "horizontal");
        Castle.screen("Menu", properties);
    }

    @OnClick(R.id.track_event)
    public void onTrackEventClick(Button button) {
        // Track an event and include some properties
        Map<String, String> properties = new HashMap<>();
        properties.put("type", "clothing");
        Castle.track("ordered item", properties);
    }

    @OnClick(R.id.flush)
    public void onFlushClick(Button button) {
        Castle.flush();
    }

    @OnClick(R.id.reset)
    public void onResetClick(Button button) {
        Castle.reset();
    }
}
