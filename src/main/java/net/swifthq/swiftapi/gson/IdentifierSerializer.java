package net.swifthq.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class IdentifierSerializer implements JsonDeserializer<Identifier>, JsonSerializer<Identifier> {

    @Override
    public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Identifier(json.getAsString());
    }

    @Override
    public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
