/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import io.castle.android.api.model.Event;
import io.castle.android.api.model.IdentifyEvent;
import io.castle.android.api.model.ScreenEvent;

public class EventAdapter implements JsonSerializer<Event>, JsonDeserializer<Event> {
    Gson gson = new Gson();

    @Override
    public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = (JsonObject) new Gson().toJsonTree(src, typeOfSrc);

        // XXXAus: We should do this a better way.
        if (src instanceof IdentifyEvent) {
            jsonObject.add("traits", jsonObject.get("properties"));
            jsonObject.remove("properties");
        } else if (src instanceof ScreenEvent) {
            jsonObject.add("name", jsonObject.get("event"));
            jsonObject.remove("event");
        }

        return jsonObject;
    }

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.get("type").getAsString().equals(Event.EVENT_TYPE_IDENTIFY)) {
            typeOfT = IdentifyEvent.class;
        } else if (jsonObject.get("type").getAsString().equals(Event.EVENT_TYPE_SCREEN)) {
            typeOfT = ScreenEvent.class;
        }
        return gson.fromJson(json, typeOfT);
    }
}
