/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;
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

class EventAdapter implements JsonSerializer<Event>, JsonDeserializer<Event> {

    private static final Gson gson = new Gson();

    @Override
    public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = (JsonObject) gson.toJsonTree(src, typeOfSrc);

        // XXXAus: We should do this a better way.
        if (src instanceof ScreenEvent) {
            jsonObject.add("name", jsonObject.get("event"));
            jsonObject.remove("event");
        }

        return jsonObject;
    }

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (typeOfT.equals(Event.class)) {
            String type = json.getAsJsonObject().get("type").getAsString();

            switch (type) {
                case Event.EVENT_TYPE_IDENTIFY:
                    typeOfT = IdentifyEvent.class;
                    break;
                case Event.EVENT_TYPE_SCREEN:
                    typeOfT = ScreenEvent.class;
                    break;
            }
        }

        return gson.fromJson(json, typeOfT);
    }
}
