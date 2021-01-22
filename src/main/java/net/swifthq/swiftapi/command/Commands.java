package net.swifthq.swiftapi.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v1.DispatcherRegistrationCallback;
import net.fabricmc.fabric.api.command.v1.ServerCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.swifthq.swiftapi.callbacks.lifecycle.ReloadCallback;
import net.swifthq.swiftapi.config.SwiftApiConfig;

import java.util.Collection;

import static net.fabricmc.fabric.api.command.v1.CommandManager.literal;

public class Commands {

	public static void initialize(SwiftApiConfig config) {
		DispatcherRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			if (config.enableModsCommand) {
				dispatcher.register(literal("mods")
						.executes(Commands::executeMods));

				dispatcher.register(literal("reload")
						.requires(commandSource -> commandSource.hasPermissionLevel(4))
						.executes(Commands::executeReload));

				dispatcher.register(literal("rl")
						.requires(commandSource -> commandSource.hasPermissionLevel(4))
						.executes(Commands::executeReload));
			}
		});
	}

	private static int executeReload(CommandContext<ServerCommandSource> commandContext) {
		ServerCommandSource source = commandContext.getSource();
		source.sendFeedback(new LiteralText("Calling reload callback...").setStyle(new Style().setColor(Formatting.YELLOW).setItalic(true)));
		int reloadedModCount = ReloadCallback.EVENT.invoker().onReload();
		source.sendFeedback(new LiteralText("Reloaded " + reloadedModCount + " mods!").setStyle(new Style().setColor(Formatting.GREEN).setItalic(true)));
		return 0;
	}

	private static int executeMods(CommandContext<ServerCommandSource> commandContext) {
		ServerCommandSource source = commandContext.getSource();
		Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
		Text feedbackText = new LiteralText(mods.size() + " Mods loaded: ").setStyle(new Style().setBold(true));
		for (ModContainer mod : mods) {
			String name = mod.getMetadata().getName();
			feedbackText.append(new LiteralText(name).setStyle(new Style()
					.setColor(Formatting.GREEN)
					.setBold(false)));
			feedbackText.append(new LiteralText(", ").setStyle(new Style().setColor(Formatting.WHITE)));
		}
		source.sendFeedback(feedbackText);
		return 0;
	}
}
