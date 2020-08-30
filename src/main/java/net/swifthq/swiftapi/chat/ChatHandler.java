package net.swifthq.swiftapi.chat;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.swifthq.swiftapi.player.SwPlayer;

/**
 * basic handler for handling chat messages.
 */
public interface ChatHandler {
    String onChatMessage(String message, ServerPlayerEntity player);
}
