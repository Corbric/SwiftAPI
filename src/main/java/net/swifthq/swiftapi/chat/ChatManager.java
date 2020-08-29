package net.swifthq.swiftapi.chat;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.swifthq.swiftapi.core.SwiftManager;

public class ChatManager {

    private static ChatManager INSTANCE;
    private ChatHandler chatHandler;

    public ChatManager(){
        INSTANCE = this;
        chatHandler = new BasicChatHandler();
    }

    public void setChatHandler(ChatHandler handler){
        this.chatHandler = handler;
    }

    public static ChatManager getInstance() {
        return INSTANCE;
    }

    public Text handleChat(String text, ServerPlayerEntity player) {
        return new LiteralText(chatHandler.onChatMessage(text, SwiftManager.getInstance().playerMap.get(player)));
    }
}
