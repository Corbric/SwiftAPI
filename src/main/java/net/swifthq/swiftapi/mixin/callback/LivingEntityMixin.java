package net.swifthq.swiftapi.mixin.callback;

import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.swifthq.swiftapi.callbacks.entity.damage.EntityDamageCallback;
import net.swifthq.swiftapi.callbacks.entity.player.damage.PlayerDamageCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity) {
            ActionResult result = PlayerDamageCallback.EVENT.invoker().onPlayerDamage((ServerPlayerEntity) (Object) this, source, amount);
            if (result == ActionResult.FAIL) {
                cir.setReturnValue(false);
                return;
            }
        }
        if (source.getAttacker() != null) {
            ActionResult result = EntityDamageCallback.EVENT.invoker().onEntityDamageEntity(((LivingEntity) (Object) this), source, amount, source.getAttacker());
            if (result == ActionResult.FAIL) {
                cir.setReturnValue(false);
            }
        }
    }

}
