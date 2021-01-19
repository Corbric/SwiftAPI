package net.swifthq.swiftapi.chat;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class BasicChatHandler implements ChatHandler {
    @Override
    public String onChatMessage(String message, ServerPlayerEntity player) {
        return player.getName().asString() + Formatting.DARK_GRAY + " » " + Formatting.RESET + message.replace("&", "\u00A7");
    }
}
