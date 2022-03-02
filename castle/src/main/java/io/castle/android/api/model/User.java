/*
 * Copyright (c) 2022 Castle
 */

package io.castle.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.castle.android.Castle;
import io.castle.android.CastleLogger;

public class User {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("email")
    String email;
    @SerializedName("phone")
    String phone;
    @SerializedName("registered_at")
    String registeredAt;
    @SerializedName("signature")
    String signature;
    @SerializedName("traits")
    Map<String, Object> traits;

    public static User userWithId(String userId, String signature, Map<String, Object> traits) {
        if(userId == null) {
            CastleLogger.e("User id can't be null.");
            return null;
        }

        if(userId.length() == 0) {
            CastleLogger.e("User id can't be empty.");
            return null;
        }

        if(signature == null) {
            CastleLogger.e("User signature can't be null.");
            return null;
        }

        if(signature.length() == 0) {
            CastleLogger.e("User signature can't be empty.");
            return null;
        }

        boolean valid = Model.propertiesContainValidData(traits);
        if(traits != null && !valid) {
            CastleLogger.e("Traits dictionary contains invalid data. Supported types are: String, Int, Map and Null");
            return null;
        }

        User user = new User();
        user.id = userId;
        user.name = (String) traits.get("name");
        user.email = (String) traits.get("email");
        user.phone = (String) traits.get("phone");
        user.registeredAt = (String) traits.get("registered_at");
        user.signature = signature;
        user.traits = traits;

        return user;
    }

    public static User userWithId(String userId, String signature) {
        return userWithId(userId, signature, new HashMap<>());
    }

    public String encode() {
        return Castle.encodeUser(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return Objects.equals(((User) obj).email, email) &&
                    Objects.equals(((User) obj).id, id) &&
                    Objects.equals(((User) obj).registeredAt, registeredAt) &&
                    Objects.equals(((User) obj).name, name) &&
                    Objects.equals(((User) obj).phone, phone) &&
                    Objects.equals(((User) obj).traits, traits);
        }
        return super.equals(obj);
    }
}
