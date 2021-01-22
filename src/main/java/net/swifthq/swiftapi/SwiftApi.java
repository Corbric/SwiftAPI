package net.swifthq.swiftapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.DispatcherRegistrationCallback;
import net.fabricmc.fabric.api.command.v1.ServerCommandSource;
import net.fabricmc.fabric.api.event.server.FabricCommandRegisteredCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.LowercaseEnumTypeAdapterFactory;
import net.minecraft.util.math.BlockPos;
import net.swifthq.swiftapi.callbacks.lifecycle.ReloadCallback;
import net.swifthq.swiftapi.chat.ChatManager;
import net.swifthq.swiftapi.command.Commands;
import net.swifthq.swiftapi.config.ConfigManager;
import net.swifthq.swiftapi.config.SwiftApiConfig;
import net.swifthq.swiftapi.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Optional;

import static net.fabricmc.fabric.api.command.v1.CommandManager.literal;

public class SwiftApi implements ModInitializer {

	public static final String MODID = "swiftapi";
	public static final Logger LOGGER = LogManager.getLogger("Swift API");
	public static final Gson GSON = createDefaultBuilder().create();
	public static SwiftApiConfig config;

	/**
	 * Creates a GsonBuilder which has serializers for a variety of Minecraft's data types
	 *
	 * <ul>
	 *     <li>{@link BlockPos} via {@link BlockPosSerializer}/li>
	 *     <li>{@link Identifier} via {@link IdentifierSerializer}/li>
	 *     <li>{@link ItemStack} via {@link ItemStackSerializer}/li>
	 *     <li>{@link Tag Tags} via {@link TagBasedSerializer}/li>
	 *     <li>{@link BlockState blockstates} via {@link BlockStateSerializer}/li>
	 *     <li>{@link Block} via {@link RegistryBasedSerializer} with {@link Block#REGISTRY}/li>
	 *     <li>{@link Item} via {@link RegistryBasedSerializer} with {@link Item#REGISTRY}/li>
	 *     <li>{@link Text} via {@link Text.Serializer}</li>
	 *     <li>{@link Style} via {@link Style.Serializer}</li>
	 * </ul>
	 */
	public static GsonBuilder createDefaultBuilder() {
		return new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(BlockPos.class, new BlockPosSerializer())
				.registerTypeAdapter(Identifier.class, new IdentifierSerializer())
				.registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
				.registerTypeHierarchyAdapter(Tag.class, new TagBasedSerializer())
				.registerTypeHierarchyAdapter(BlockState.class, new BlockStateSerializer())
				// Registry based
				.registerTypeHierarchyAdapter(Block.class, new RegistryBasedSerializer<>(Block.REGISTRY))
				.registerTypeHierarchyAdapter(Item.class, new RegistryBasedSerializer<>(Item.REGISTRY))
				// Text (from Text.Serializer)
				.registerTypeHierarchyAdapter(Text.class, new Text.Serializer())
				.registerTypeHierarchyAdapter(Style.class, new Style.Serializer())
				.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory());
	}

	/**
	 * Used internally for debug information
	 *
	 * @return the mods version
	 */
	public static String getVersion() {
		return FabricLoader.getInstance().getModContainer(MODID)
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElseThrow(() -> new RuntimeException("Failed to grab mod version info"));
	}

	private void initializeConfig() {
		Optional<SwiftApiConfig> optional = ConfigManager.read("config", SwiftApiConfig.class);
		config = optional.orElseGet(SwiftApiConfig::new);
		ConfigManager.write("config", config);
	}

	@Override
	public void onInitialize() {
		new ChatManager();
		initializeConfig();
		Commands.initialize(config);
		LOGGER.info("Swift API loaded!");
		LOGGER.info("Running version: " + getVersion());
	}
}
