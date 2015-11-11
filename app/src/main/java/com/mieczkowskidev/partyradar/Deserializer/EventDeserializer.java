package com.mieczkowskidev.partyradar.Deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mieczkowskidev.partyradar.Objects.Event;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk Mieczkowski on 2015-11-11.
 */
public class EventDeserializer implements JsonDeserializer<List<Event>> {

    @Override
    public List<Event> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        List<Event> list = new ArrayList<>();

        JsonObject mainObject = json.getAsJsonObject();
        JsonArray eventArray = mainObject.get("").getAsJsonArray();

        for (JsonElement jsonElement : eventArray) {

            int id = jsonElement.getAsJsonObject().get("id").getAsInt();
            String user = jsonElement.getAsJsonObject().get("user").getAsString();
            String photo = jsonElement.getAsJsonObject().get("photo").getAsString();
            String description = jsonElement.getAsJsonObject().get("description").getAsString();
            Double lat = jsonElement.getAsJsonObject().get("lat").getAsDouble();
            Double lon = jsonElement.getAsJsonObject().get("lon").getAsDouble();
            String created = jsonElement.getAsJsonObject().get("created").getAsString();
            String modified = jsonElement.getAsJsonObject().get("modified").getAsString();

            Event event = new Event(id, user, photo, description, lat, lon, created, modified);
            list.add(event);
        }

        return list;
    }
}
