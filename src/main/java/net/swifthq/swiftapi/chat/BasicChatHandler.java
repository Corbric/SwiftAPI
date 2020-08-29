package net.swifthq.swiftapi.chat;

import net.swifthq.swiftapi.ChatColor;
import net.swifthq.swiftapi.player.Player;

public class BasicChatHandler implements ChatHandler {
    @Override
    public String onChatMessage(String message, Player player) {
        return player.getDisplayName() + ChatColor.DARK_GRAY +  " » " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message);
    }
}
