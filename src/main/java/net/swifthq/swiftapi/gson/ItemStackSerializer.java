package net.swifthq.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemStackSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("damage", new JsonPrimitive(src.getDamage()));
        object.add("itemName", new JsonPrimitive(src.getName()));
        object.add("item", context.serialize(Item.REGISTRY.getIdentifier(src.getItem())));
        object.add("lore", context.serialize(getLore(src)));
        return object;
    }


    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonElement identifier = object.get("identifier");
        int damage = object.get("damage").getAsInt();
        String name = object.get("itemName").getAsString();
        List<String> lore = context.deserialize(object.get("lore"), List.class);

        ItemStack itemStack = new ItemStack(Item.REGISTRY.get(context.deserialize(identifier, Identifier.class)));
        itemStack.setCustomName(name);
        itemStack.setDamage(damage);
        setLore(lore, itemStack);
        return itemStack;
    }

    /**
     * sets the lore of an itemstack
     *
     * @param lore  the lore to set
     * @param stack the itemStack
     */
    public static void setLore(List<String> lore, ItemStack stack) {
        CompoundTag displayTag = stack.tag.getCompound("display");
        ListTag newLore = new ListTag();
        for (String loreLine : lore) {
            StringTag line = new StringTag(loreLine);
            newLore.add(line);
        }
        displayTag.remove("lore");
        displayTag.put("lore", newLore);
    }

    /**
     * gets the lore of an itemstack
     *
     * @param stack the itemstack
     * @return the lore
     */
    public static List<String> getLore(ItemStack stack) {
        CompoundTag displayTag = stack.tag.getCompound("display");
        ListTag oldLore = displayTag.getList("lore", 8);
        List<String> lore = new ArrayList<>();
        for (int i = 0; i < oldLore.size(); i++) {
            lore.add(oldLore.getString(i));
        }
        return lore;
    }
}
