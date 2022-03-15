/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.castle.android.api.model.CustomEvent;
import io.castle.android.api.model.Event;
import io.castle.android.api.model.ScreenEvent;

class EventAdapter implements JsonDeserializer<Event> {

    private static final Gson gson = new Gson();

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (typeOfT.equals(Event.class)) {
            String type = json.getAsJsonObject().get("type").getAsString();

            switch (type) {
                case Event.EVENT_TYPE_SCREEN:
                    typeOfT = ScreenEvent.class;
                    break;
                case Event.EVENT_TYPE_CUSTOM:
                    typeOfT = CustomEvent.class;
                    break;
            }
        }

        return gson.fromJson(json, typeOfT);
    }
}
