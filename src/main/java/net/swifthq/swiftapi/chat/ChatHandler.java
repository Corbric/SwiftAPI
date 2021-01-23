package net.swifthq.swiftapi.chat;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * basic handler for handling chat messages.
 */
public interface ChatHandler {
    String onChatMessage(String message, ServerPlayerEntity player);
}
