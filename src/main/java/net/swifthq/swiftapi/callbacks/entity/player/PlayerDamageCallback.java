package net.swifthq.swiftapi.callbacks.entity.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface PlayerDamageCallback {

    Event<PlayerDamageCallback> EVENT = EventFactory.createArrayBacked(PlayerDamageCallback.class, (listeners) -> (entity, damageSource, originalHealth, damage, attacker) -> {
        for (PlayerDamageCallback callback : listeners) {
            if (callback.onPlayerDamageEntity(entity, damageSource, originalHealth, damage, attacker) == ActionResult.FAIL) {
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult onPlayerDamageEntity(LivingEntity entity, DamageSource damageSource, float originalHealth, float damage, Entity attacker);

    ActionResult onPlayerDamagePlayer(ServerPlayerEntity entity, DamageSource damageSource, float originalHealth, float damage, ServerPlayerEntity attacker);
}
