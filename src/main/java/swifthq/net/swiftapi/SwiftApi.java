package swifthq.net.swiftapi;

import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import swifthq.net.swiftapi.core.SwiftManager;
import swifthq.net.swiftapi.gson.BlockPosSerializer;
import swifthq.net.swiftapi.gson.EnchantmentSerializer;
import swifthq.net.swiftapi.gson.IdentifierSerializer;
import swifthq.net.swiftapi.gson.ItemStackSerializer;

import java.util.logging.Logger;

public class SwiftApi implements ModInitializer {
	@Override
	public void onInitialize() {
		new SwiftManager();
		Logger.getLogger("SwiftApi").info("Swift API loaded!");
	}

	/**
	 * creates a GsonBuilder with the {@link BlockPos}, {@link Enchantment}, {@link Identifier} and {@link ItemStack} serializers to make configs easier to work with
	 * @return a configured {@link GsonBuilder}
	 */
	public static GsonBuilder createDefaultBuilder(){
		return new GsonBuilder()
				.registerTypeAdapter(BlockPos.class, new BlockPosSerializer())
				.registerTypeAdapter(Enchantment.class, new EnchantmentSerializer())
				.registerTypeAdapter(Identifier.class, new IdentifierSerializer())
				.registerTypeAdapter(ItemStack.class, new ItemStackSerializer());
	}
}
