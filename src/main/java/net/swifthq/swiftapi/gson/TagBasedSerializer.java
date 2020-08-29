package net.swifthq.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.nbt.*;

import java.lang.reflect.Type;
import java.util.Map;

public class TagBasedSerializer implements JsonSerializer<Tag>, JsonDeserializer<Tag> {

    @Override
    public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // This shouldn't even be possible
        if (json instanceof JsonNull) {
            return null;
        }

        JsonObject object = json.getAsJsonObject();

        switch (object.get("type").getAsString()) {
            case "s":
                return new ShortTag(object.get("value").getAsShort());
            case "d":
                return new DoubleTag(object.get("value").getAsDouble());
            case "f":
                return new FloatTag(object.get("value").getAsFloat());
            case "b":
                return new ByteTag(object.get("value").getAsByte());
            case "i":
                return new IntTag(object.get("value").getAsInt());
            case "l":
                return new LongTag(object.get("value").getAsLong());
            case "ba":
                return new ByteArrayTag(context.deserialize(object.get("value"), byte[].class));
            case "c":
                CompoundTag compoundTag = new CompoundTag();

                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    String key = entry.getKey();

                    if (key.startsWith("$")) {
                        compoundTag.put(key.substring(1), context.deserialize(entry.getValue(), Tag.class));
                    }
                }

                return compoundTag;
            case "str":
                return new StringTag(object.get("value").getAsString());
            case "ia":
                return new IntArrayTag(context.deserialize(object.get("value"), int[].class));
            case "li":
                ListTag listTag = new ListTag();

                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    String key = entry.getKey();

                    if (key.startsWith("@")) {
                        listTag.add(context.deserialize(entry.getValue(), Tag.class));
                    }
                }

                return listTag;
        }

        throw new IllegalStateException("Unknown tag type");
    }

    @Override
    public JsonElement serialize(Tag src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        if (src instanceof ShortTag) {
            object.addProperty("type", "s");
            object.addProperty("value", ((ShortTag) src).getShort());
        } else if (src instanceof DoubleTag) {
            object.addProperty("type", "d");
            object.addProperty("value", ((DoubleTag) src).getDouble());
        } else if (src instanceof FloatTag) {
            object.addProperty("type", "f");
            object.addProperty("value", ((FloatTag) src).getFloat());
        } else if (src instanceof ByteTag) {
            object.addProperty("type", "b");
            object.addProperty("value", ((ByteTag) src).getByte());
        } else if (src instanceof IntTag) {
            object.addProperty("type", "i");
            object.addProperty("value", ((IntTag) src).getInt());
        } else if (src instanceof LongTag) {
            object.addProperty("type", "l");
            object.addProperty("value", ((LongTag) src).getLong());
        } else if (src instanceof ByteArrayTag) {
            object.addProperty("type", "ba");
            object.add("value", context.serialize(((ByteArrayTag) src).getArray()));
        } else if (src instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) src;
            object.addProperty("type", "c");

            for (String key : compoundTag.getKeys()) {
                object.add("$" + key, context.serialize(compoundTag.get(key)));
            }
        } else if (src instanceof StringTag) {
            object.addProperty("type", "str");
            object.addProperty("value", ((StringTag) src).asString());
        } else if (src instanceof IntArrayTag) {
            object.addProperty("type", "ia");
            object.add("value", context.serialize(((IntArrayTag) src).getIntArray()));
        } else if (src instanceof ListTag) {
            ListTag listTag = (ListTag) src;
            object.addProperty("type", "li");

            for (int i = 0; i < listTag.size(); i++) {
                object.add("@" + i, context.serialize(listTag.get(i)));
            }
        } else {
            return JsonNull.INSTANCE;
        }

        return object;
    }
}
