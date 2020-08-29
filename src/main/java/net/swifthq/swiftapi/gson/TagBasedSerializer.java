package net.swifthq.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.nbt.Tag;

import java.lang.reflect.Type;

public class TagBasedSerializer implements JsonSerializer<Tag>, JsonDeserializer<Tag> {

    @Override
    public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return StringNbtReader.parse(json.getAsString());
        } catch (NbtException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Tag src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
