package io.castle.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Copyright (c) 2017 Castle
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("TestActivity");
    }
}
