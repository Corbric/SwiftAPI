package net.swifthq.swiftapi.mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickWindowC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.swifthq.swiftapi.callbacks.ClickContainerCallback;
import net.swifthq.swiftapi.chat.ChatManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayerNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Shadow @Final private MinecraftServer server;

    @Shadow private int messageCooldown;

    @Shadow public abstract void disconnect(String reason);

    @Inject(method = "onClickWindow", at = @At("HEAD"), cancellable = true)
    public void clickWindow(ClickWindowC2SPacket packet, CallbackInfo ci) {
        if (!ClickContainerCallback.EVENT.invoker().clickContainer(player, packet)) {
            ci.cancel();
        }
    }

    @Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Z)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void handleChat(ChatMessageC2SPacket packet, CallbackInfo ci, String string, Text text){
        ci.cancel();
        this.server.getPlayerManager().broadcastChatMessage(ChatManager.getInstance().handleChat(string, player), false);


        // finish off what we cancel
        this.messageCooldown += 20;
        if (this.messageCooldown > 200 && !this.server.getPlayerManager().isOperator(this.player.getGameProfile())) {
            this.disconnect("disconnect.spam");
        }
    }
}
