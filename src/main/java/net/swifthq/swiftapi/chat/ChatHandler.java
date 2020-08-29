package net.swifthq.swiftapi.chat;

import net.swifthq.swiftapi.player.Player;

/**
 * basic handler for handling chat messages.
 */
public interface ChatHandler {
    String onChatMessage(String message, Player player);
}
