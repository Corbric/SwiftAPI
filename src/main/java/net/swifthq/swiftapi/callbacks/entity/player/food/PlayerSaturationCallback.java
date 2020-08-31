package net.swifthq.swiftapi.callbacks.entity.player.food;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface PlayerSaturationCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<PlayerSaturationCallback> EVENT = EventFactory.createArrayBacked(PlayerSaturationCallback.class, (listeners) -> (playerEntity, prevFoodLevel, foodLevel) -> {
        for (PlayerSaturationCallback callback : listeners) {
            if(callback.playerJoin(playerEntity, prevFoodLevel, foodLevel) == ActionResult.PASS){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult playerJoin(PlayerEntity playerEntity, int prevFoodLevel, int foodLevel);
}
