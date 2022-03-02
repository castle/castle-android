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

import io.castle.android.api.model.Custom;
import io.castle.android.api.model.Model;
import io.castle.android.api.model.ScreenEvent;

class EventAdapter implements JsonDeserializer<Model> {

    private static final Gson gson = new Gson();

    @Override
    public Model deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (typeOfT.equals(Model.class)) {
            String type = json.getAsJsonObject().get("type").getAsString();

            switch (type) {
                case Model.EVENT_TYPE_SCREEN:
                    typeOfT = ScreenEvent.class;
                    break;
                case Model.EVENT_TYPE_CUSTOM:
                    typeOfT = Custom.class;
                    break;
            }
        }

        return gson.fromJson(json, typeOfT);
    }
}
