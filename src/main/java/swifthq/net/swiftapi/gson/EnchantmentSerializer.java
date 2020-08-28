package swifthq.net.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;

public class EnchantmentSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
