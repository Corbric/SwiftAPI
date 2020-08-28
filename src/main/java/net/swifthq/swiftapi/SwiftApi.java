package net.swifthq.swiftapi;

import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.swifthq.swiftapi.core.SwiftManager;
import net.swifthq.swiftapi.gson.BlockPosSerializer;
import net.swifthq.swiftapi.gson.EnchantmentSerializer;
import net.swifthq.swiftapi.gson.IdentifierSerializer;
import net.swifthq.swiftapi.gson.ItemStackSerializer;

public class SwiftApi implements ModInitializer {
    @Override
    public void onInitialize() {
        new SwiftManager();
        System.out.println("Swift API loaded!");
    }

    /**
     * creates a GsonBuilder with the {@link BlockPos}, {@link Enchantment}, {@link Identifier} and {@link ItemStack} serializers to make configs easier to work with
     *
     * @return a configured {@link GsonBuilder}
     */
    public static GsonBuilder createDefaultBuilder() {
        return new GsonBuilder()
                .registerTypeAdapter(BlockPos.class, new BlockPosSerializer())
                .registerTypeAdapter(Enchantment.class, new EnchantmentSerializer())
                .registerTypeAdapter(Identifier.class, new IdentifierSerializer())
                .registerTypeAdapter(ItemStack.class, new ItemStackSerializer());
    }
}
