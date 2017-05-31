package io.castle.android.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import io.castle.android.api.model.Event;
import io.castle.android.api.model.IdentifyEvent;
import io.castle.android.api.model.ScreenEvent;

/**
 * Copyright (c) 2017 Castle
 */
public class EventAdapter implements JsonSerializer<Event> {
    @Override
    public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = (JsonObject) new Gson().toJsonTree(src, typeOfSrc);

        if (src instanceof IdentifyEvent) {
            jsonObject.add("traits", jsonObject.get("properties"));
            jsonObject.remove("properties");
        } else if (src instanceof ScreenEvent) {
            jsonObject.add("name", jsonObject.get("event"));
            jsonObject.remove("event");
        }

        return jsonObject;
    }
}
