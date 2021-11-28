package net.swifthq.swiftapi.callbacks.entity.damage;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface EntityDamageCallback {

    Event<EntityDamageCallback> EVENT = EventFactory.createArrayBacked(EntityDamageCallback.class, (listeners) -> (entity, damageSource, damage, attacker) -> {
        for (EntityDamageCallback callback : listeners) {
            if(callback.onEntityDamageEntity(entity, damageSource, damage, attacker) == ActionResult.FAIL){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult onEntityDamageEntity(LivingEntity entity, DamageSource damageSource, float damage, Entity attacker);
}
