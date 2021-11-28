package net.swifthq.swiftapi.callbacks.inventory;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.server.network.ServerPlayerEntity;

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
