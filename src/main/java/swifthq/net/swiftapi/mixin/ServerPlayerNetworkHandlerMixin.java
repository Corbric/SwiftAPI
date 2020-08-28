package swifthq.net.swiftapi.mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClickWindowC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import swifthq.net.swiftapi.callbacks.ClickContainerCallback;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayerNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onClickWindow", at = @At("HEAD"), cancellable = true)
    public void clickWindow(ClickWindowC2SPacket packet, CallbackInfo ci){
        if(!ClickContainerCallback.EVENT.invoker().clickContainer(player, packet)){
            ci.cancel();
        }
    }

}
