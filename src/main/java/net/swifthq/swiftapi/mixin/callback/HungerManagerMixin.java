package net.swifthq.swiftapi.mixin.callback;

import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.swifthq.swiftapi.callbacks.entity.player.food.PlayerSaturationCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Shadow
    private int prevFoodLevel;

    @Shadow
    private int foodLevel;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void callHungerCallback(PlayerEntity playerEntity, CallbackInfo ci) {
        if (PlayerSaturationCallback.EVENT.invoker().playerJoin(playerEntity, prevFoodLevel, foodLevel) != ActionResult.SUCCESS) {
            ci.cancel();
        }
    }
}
