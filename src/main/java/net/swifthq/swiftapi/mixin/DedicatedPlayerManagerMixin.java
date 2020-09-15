package net.swifthq.swiftapi.mixin;

import net.minecraft.server.dedicated.DedicatedPlayerManager;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DedicatedPlayerManager.class)
public class DedicatedPlayerManagerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(DedicatedServer server, CallbackInfo ci){

    }

}
