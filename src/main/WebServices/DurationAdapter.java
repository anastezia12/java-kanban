package main.WebServices;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(duration.toMinutes());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int minutes = json.getAsInt();
        return Duration.ofMinutes(minutes);
    }
}