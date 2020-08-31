package net.swifthq.swiftapi.callbacks.entity.player.damage;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface PlayerDamageCallback {

    Event<PlayerDamageCallback> EVENT = EventFactory.createArrayBacked(PlayerDamageCallback.class, (listeners) -> (entity, damageSource, originalHealth, damage) -> {
        for (PlayerDamageCallback callback : listeners) {
            if (callback.onPlayerDamage(entity, damageSource, originalHealth, damage) == ActionResult.FAIL) {
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult onPlayerDamage(ServerPlayerEntity entity, DamageSource damageSource, float originalHealth, float damage);
}
