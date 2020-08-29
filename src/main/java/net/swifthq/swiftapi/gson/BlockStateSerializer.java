package net.swifthq.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.lang.reflect.Type;

public class BlockStateSerializer implements JsonSerializer<BlockState>, JsonDeserializer<BlockState> {

    @Override
    public BlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Block block = context.deserialize(object.get("block"), Block.class);
        return block.stateFromData(object.get("data").getAsInt());
    }

    @Override
    public JsonElement serialize(BlockState src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("block", context.serialize(src.getBlock()));
        object.addProperty("data", src.getBlock().getData(src));
        return object;
    }
}
