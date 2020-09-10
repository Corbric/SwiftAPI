package net.swifthq.swiftapi.callbacks.entity.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface PlayerItemInteractCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<PlayerItemInteractCallback> EVENT = EventFactory.createArrayBacked(PlayerItemInteractCallback.class, (listeners) -> (player, item) -> {
        for (PlayerItemInteractCallback callback : listeners) {
            if(callback.interactItem(player, item) == ActionResult.FAIL){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult interactItem(PlayerEntity player, ItemStack item);
}
