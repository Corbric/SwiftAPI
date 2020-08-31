package net.swifthq.swiftapi.callbacks.entity.damage;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface EntityDamageCallback {

    Event<EntityDamageCallback> EVENT = EventFactory.createArrayBacked(EntityDamageCallback.class, (listeners) -> (entity, damageSource, originalHealth, damage, attacker) -> {
        for (EntityDamageCallback callback : listeners) {
            if(callback.onEntityDamageEntity(entity, damageSource, originalHealth, damage, attacker) == ActionResult.FAIL){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult onEntityDamageEntity(LivingEntity entity, DamageSource damageSource, float originalHealth, float damage, Entity attacker);
}
