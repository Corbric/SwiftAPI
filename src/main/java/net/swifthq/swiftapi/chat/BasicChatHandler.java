package net.swifthq.swiftapi.chat;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.swifthq.swiftapi.ChatColor;
import net.swifthq.swiftapi.player.SwPlayer;

public class BasicChatHandler implements ChatHandler {
    @Override
    public String onChatMessage(String message, ServerPlayerEntity player) {
        return player.getName().asString() + ChatColor.DARK_GRAY + " » " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message);
    }
}
