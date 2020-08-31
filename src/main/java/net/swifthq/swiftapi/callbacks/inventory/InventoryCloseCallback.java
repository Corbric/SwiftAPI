package net.swifthq.swiftapi.callbacks.inventory;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface InventoryCloseCallback {

    Event<InventoryCloseCallback> EVENT = EventFactory.createArrayBacked(InventoryCloseCallback.class, (listeners) -> (inventory) -> {
        for (InventoryCloseCallback callback : listeners) {
            if(callback.onCloseInventory(inventory) == ActionResult.FAIL){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult onCloseInventory(ServerPlayerEntity inventory);
}
