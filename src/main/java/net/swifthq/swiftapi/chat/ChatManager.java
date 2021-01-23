package net.swifthq.swiftapi.chat;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ChatManager {

    private static ChatManager INSTANCE;
    private ChatHandler chatHandler;

    public ChatManager() {
        INSTANCE = this;
        chatHandler = new BasicChatHandler();
    }

    public static ChatManager getInstance() {
        return INSTANCE;
    }

    public void setChatHandler(ChatHandler handler) {
        this.chatHandler = handler;
    }

    public Text handleChat(String text, ServerPlayerEntity player, MinecraftServer server) {
        return new LiteralText(chatHandler.onChatMessage(text, player));
    }
}
