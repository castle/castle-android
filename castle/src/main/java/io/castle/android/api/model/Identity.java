package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Copyright (c) 2017 Castle
 */

public class Identity {
    @SerializedName("user_id")
    String userId;
    HashMap<String, String> traits = new HashMap<>();

    public Identity(String userId) {
        this.userId = userId;
    }

    public Identity(String userId, HashMap<String, String> traits) {
        this.userId = userId;
        this.traits = traits;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addTrait(String key, String value) {
        traits.put(key, value);
    }

    public void removeTrait(String key) {
        traits.remove(key);
    }
}
