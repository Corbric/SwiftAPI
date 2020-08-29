package net.swifthq.swiftapi;

import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerPlayerEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.swifthq.swiftapi.callbacks.PlayerJoinCallback;
import net.swifthq.swiftapi.core.SwiftManager;
import net.swifthq.swiftapi.gson.BlockPosSerializer;
import net.swifthq.swiftapi.gson.EnchantmentSerializer;
import net.swifthq.swiftapi.gson.IdentifierSerializer;
import net.swifthq.swiftapi.gson.ItemStackSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class SwiftApi implements ModInitializer {

    public static final String MODID = "swiftapi";
    public static final Logger LOGGER = LogManager.getLogger("Swift API");

    @Override
    public void onInitialize() {
        new SwiftManager();

        //Register callback for player register
        PlayerJoinCallback.EVENT.register(player -> SwiftManager.getInstance().addPlayer(player));
        ServerPlayerEvents.DISCONNECT.register((clientConnection, player, minecraftServer) -> SwiftManager.getInstance().removePlayer(player));

        LOGGER.info("Swift API loaded!");
        LOGGER.info("Running version: " + getVersion());
    }

    /**
     * used internally for debug information
     *
     * @return the mods version
     */
    public String getVersion() {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(MODID);
        if(modContainer.isPresent()){
            return modContainer.get().getMetadata().getVersion().getFriendlyString();
        }else {
            throw new RuntimeException("Failed to grab mod version info");
        }
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
