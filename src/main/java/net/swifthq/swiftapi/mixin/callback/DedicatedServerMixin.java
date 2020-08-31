package net.swifthq.swiftapi.mixin.callback;

import net.minecraft.server.dedicated.DedicatedServer;
import net.swifthq.swiftapi.callbacks.world.WorldReadyCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(method = "setupServer", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", ordinal = 7)) //7th usage of Logger;info (counting from 0). put this here to help people understand mixin
    public void cool(CallbackInfoReturnable<Boolean> cir){
        WorldReadyCallback.EVENT.invoker().worldReady();
    }

}
