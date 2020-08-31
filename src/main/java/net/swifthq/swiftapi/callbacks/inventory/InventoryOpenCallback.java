package net.swifthq.swiftapi.callbacks.inventory;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.inventory.Inventory;
import net.swifthq.swiftapi.dimension.DimensionRegistry;

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
