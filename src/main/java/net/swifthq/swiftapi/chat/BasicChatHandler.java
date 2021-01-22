package net.swifthq.swiftapi.chat;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class BasicChatHandler implements ChatHandler {
    @Override
    public String onChatMessage(String vanilla, ServerPlayerEntity player) {
        String message = player.getName().asString() + Formatting.DARK_GRAY + " » " + Formatting.RESET + vanilla.replace("&", "\u00A7");
        player.server.getPlayerManager().broadcastChatMessage(new LiteralText(message), false);
        return message;
    }
}
