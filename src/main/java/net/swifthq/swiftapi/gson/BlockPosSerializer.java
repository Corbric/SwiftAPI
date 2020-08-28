package net.swifthq.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;

public class BlockPosSerializer implements JsonSerializer<BlockPos>, JsonDeserializer<BlockPos>{
    @Override
    public JsonElement serialize(BlockPos src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("x", new JsonPrimitive(src.getX()));
        object.add("y", new JsonPrimitive(src.getY()));
        object.add("z", new JsonPrimitive(src.getZ()));

        return object;
    }

    @Override
    public BlockPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return new BlockPos(object.get("x").getAsInt(), object.get("y").getAsInt(), object.get("z").getAsInt());
    }
}
