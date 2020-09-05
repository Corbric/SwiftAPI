package net.swifthq.swiftapi.mixin.callback;

import net.minecraft.server.dedicated.DedicatedServer;
import net.swifthq.swiftapi.callbacks.world.PostWorldReadyCallback;
import net.swifthq.swiftapi.callbacks.world.PreWorldReadyCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(method = "setupServer", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", ordinal = 7)) //7th usage of Logger;info (counting from 0). put this here to help people understand mixin
    public void pre(CallbackInfoReturnable<Boolean> cir){
        PreWorldReadyCallback.EVENT.invoker().worldReady();
    }

    @Inject(method = "setupServer", at = @At("RETURN"))
    public void post(CallbackInfoReturnable<Boolean> cir){
        PostWorldReadyCallback.EVENT.invoker().worldReady();
    }

}
