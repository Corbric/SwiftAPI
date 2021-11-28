package net.swifthq.swiftapi.callbacks.entity.player.food;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

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
