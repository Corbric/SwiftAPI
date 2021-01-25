package net.swifthq.swiftapi.callbacks.entity.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface ItemInteractCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<ItemInteractCallback> EVENT = EventFactory.createArrayBacked(ItemInteractCallback.class, (listeners) -> (player, pos, item) -> {
        for (ItemInteractCallback callback : listeners) {
            if(callback.interactItem(player, pos, item) == ActionResult.FAIL){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult interactItem(PlayerEntity player, BlockPos pos, ItemStack item);
}
