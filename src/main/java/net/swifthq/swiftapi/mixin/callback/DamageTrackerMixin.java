package net.swifthq.swiftapi.mixin.callback;

import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.swifthq.swiftapi.callbacks.entity.damage.EntityDamageCallback;
import net.swifthq.swiftapi.callbacks.entity.player.damage.PlayerDamageCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageTracker.class)
public class DamageTrackerMixin {

    @Shadow @Final private LivingEntity entity;

    @Inject(at = @At("HEAD"), method = "onDamage", cancellable = true)
    public void onDamage(DamageSource damageSource, float originalHealth, float damage, CallbackInfo info) {
        if(this.entity instanceof ServerPlayerEntity){
            ActionResult result = PlayerDamageCallback.EVENT.invoker().onPlayerDamage((ServerPlayerEntity) this.entity, damageSource, originalHealth, damage);
            if(result == ActionResult.FAIL){
                info.cancel();
            }
        }
        if(damageSource.getAttacker() != null){
            ActionResult result = EntityDamageCallback.EVENT.invoker().onEntityDamageEntity(this.entity, damageSource, originalHealth, damage, damageSource.getAttacker());
            if(result == ActionResult.FAIL){
                info.cancel();
            }
        }
    }
}
