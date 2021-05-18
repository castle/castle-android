/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;
import java.util.TimeZone;

import io.castle.android.Castle;

/**
 * Model class containing context information for a user. Included in all events
 */
public class Context {
    @SerializedName("client_id")
    String clientId;

    private Context() {
    }

    private void init() {
        this.clientId = Castle.createRequestToken();
    }

    public static Context create() {
        Context context = new Context();
        context.init();
        return context;
    }
}
