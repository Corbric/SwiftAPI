package net.swifthq.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        Item type = context.deserialize(object.get("type"), Item.class);
        CompoundTag tag = context.deserialize(object.get("tag"), CompoundTag.class);
        int count = object.get("count").getAsInt();
        int damage = object.get("damage").getAsInt();

        ItemStack stack = new ItemStack(type, count, damage);
        stack.setTag(tag);

        return stack;
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", context.serialize(src.getItem()));
        object.add("tag", context.serialize(src.tag));
        object.addProperty("count", src.count);
        object.addProperty("damage", src.getDamage());
        return object;
    }
}
