package net.swifthq.swiftapi.callbacks.inventory;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.inventory.Inventory;

public interface InventoryOpenCallback {

    Event<InventoryOpenCallback> EVENT = EventFactory.createArrayBacked(InventoryOpenCallback.class, (listeners) -> (inventory) -> {
        for (InventoryOpenCallback callback : listeners) {
            if(callback.onOpenInventory(inventory) == ActionResult.FAIL){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult onOpenInventory(Inventory inventory);
}
