/*
 * Copyright (c) 2025 Castle
 */

package io.castle.android.testsupport;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("TestActivity");
    }
}
