package net.swifthq.swiftapi.callbacks.entity.player.damage;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerDamageCallback {

    Event<PlayerDamageCallback> EVENT = EventFactory.createArrayBacked(PlayerDamageCallback.class, (listeners) -> (entity, damageSource, damage) -> {
        for (PlayerDamageCallback callback : listeners) {
            if (callback.onPlayerDamage(entity, damageSource, damage) == ActionResult.FAIL) {
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult onPlayerDamage(ServerPlayerEntity entity, DamageSource damageSource, float damage);
}
